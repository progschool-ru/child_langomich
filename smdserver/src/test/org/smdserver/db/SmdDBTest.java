package org.smdserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author serdgo
 */
public class SmdDBTest
{
	private static final String TEST_TABLE = "test1";

	private Connection connection;
	private ISmdDB db;
	private String testTable;

	@Before
    public void setUp () throws SQLException
	{
		String testConfig = ResourceBundle.getBundle("org.smdserver.config")
				                      .getString("server.test.properties.file");
		ResourceBundle rb = ResourceBundle.getBundle(testConfig);

		String url = rb.getString("db.url");
		String user = rb.getString("db.user");
		String password = rb.getString("db.password");
		connection = DriverManager.getConnection(url, user, password);
		db = new SmdDB(rb);
		testTable = rb.getString("db.tablesPrefix") + TEST_TABLE;
    }

	@After
	public void tearDown() throws SQLException
	{
		if(!connection.isClosed())
		{
			connection.close();
		}
		connection = null;
		db.close();
		db = null;
	}

	@Test
	public void testItSelf () throws Exception
	{
		boolean first = connection.isClosed();

		Statement st = connection.createStatement();
		ResultSet set = st.executeQuery("SELECT 1");
		set.next();
		int i = set.getInt(1);
		boolean isAnythingElse = set.next();

		connection.close();
		boolean second = connection.isClosed();
		
		assertFalse(first);
		assertEquals(1, i);
		assertFalse(isAnythingElse);
		assertTrue(second);
	}

	@Test
	public void testUpdateSelectSingle() throws Exception
	{
//		ISmdDB db = new SmdDB(connection);

		int numRows = getNumRows();

		String query = String.format("INSERT INTO %1$s (test_id, name) VALUE (\"1\", \"petya\");", testTable);
		boolean success = db.updateSingle(query);

		int numRowsAfterCreation = getNumRows();

		String selectQuery = String.format("SELECT * FROM %1$s WHERE test_id=\"1\";", testTable);
		TestParser parser = new TestParser();
		db.selectSingle(selectQuery, parser);

		String clearQuery = String.format("DELETE FROM %1$s WHERE test_id=\"1\";", testTable);
		Statement st = connection.createStatement();
		st.executeUpdate(clearQuery);
		st.close();
		int numRowsAfterClearing = getNumRows();

		assertEquals(numRows, numRowsAfterClearing);
		assertTrue(success);
		assertEquals(numRows + 1, numRowsAfterCreation);
		assertEquals("1", parser.id);
		assertEquals("petya", parser.name);
		assertEquals(1, parser.counter);

	}

	@Test
	public void testNullParser() throws Exception
	{
//		ISmdDB db = new SmdDB(connection);
		Statement st = connection.createStatement();

		String query = String.format("INSERT INTO %1$s (test_id, name) VALUE (\"1\", \"petya\");", testTable);
		st.executeUpdate(query);

		boolean caught = false;
		boolean success = true;
		try
		{
			String selectQuery = String.format("SELECT * FROM %1$s WHERE test_id=\"1\";", testTable);
			success = db.selectSingle(selectQuery, null);
		}
		catch(NullPointerException e)
		{
			caught = true;
		}

		String clearQuery = String.format("DELETE FROM %1$s WHERE test_id=\"1\";", testTable);
		st.executeUpdate(clearQuery);
		st.close();

		assertFalse(caught);
		assertFalse(success);
	}

	@Test
	public void testIncorrectQuery() throws Exception
	{
	//	ISmdDB db = new SmdDB(connection);

		int numRows = getNumRows();

		String query = String.format("INSERT INT %1$s (test_id, name) VALUE (\"1\", \"petya\");", testTable);
		boolean success = db.updateSingle(query);

		int numRows2 = getNumRows();

		assertFalse(success);
		assertEquals(numRows, numRows2);
	}

	@Test
	public void testEscapeString()
	{
		String dirtyString = "myName\\\"; DELETE FROM smd_users WHERE user_id=\"me\"; " +
									"DROP TABLE smd_users;";
		String dbString = "myName\\\\\\\"; DELETE FROM smd_users WHERE user_id=\\\"me\\\"; " +
									"DROP TABLE smd_users;";
		String result = db.escapeString(dirtyString);

		assertEquals(dbString, result);
	}

	private int getNumRows() throws Exception
	{
		String getCountQuery = String.format("SELECT COUNT(*) FROM %1$s", testTable);
		Statement st = connection.createStatement();
		ResultSet set = st.executeQuery(getCountQuery);
		set.next();
		int numRows = set.getInt(1);
		st.close();
		return numRows;
	}

	private class TestParser implements IResultParser
	{
		String id;
		String name;
		int counter = 0;

		public boolean parse(ResultSet result) throws SQLException
		{
			id = result.getString(1);
			name = result.getString("name");
			counter ++;
			return true;
		}
	}
}
