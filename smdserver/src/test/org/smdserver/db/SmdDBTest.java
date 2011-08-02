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
    }

	@After
	public void tearDown() throws SQLException
	{
		if(!connection.isClosed())
		{
			connection.close();
		}
		connection = null;
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
		ISmdDB db = new SmdDB(connection);

		int numRows = getNumRows(TEST_TABLE);

		String query = String.format("INSERT INTO %1$s (test_id, name) VALUE (\"1\", \"petya\");", TEST_TABLE);
		boolean success = db.updateSingle(query);

		int numRowsAfterCreation = getNumRows(TEST_TABLE);

		String selectQuery = String.format("SELECT * FROM %1$s WHERE test_id=\"1\";", TEST_TABLE);
		TestParser parser = new TestParser();
		db.selectSingle(selectQuery, parser);

		String clearQuery = String.format("DELETE FROM %1$s WHERE test_id=\"1\";", TEST_TABLE);
		Statement st = connection.createStatement();
		st.executeUpdate(clearQuery);
		int numRowsAfterClearing = getNumRows(TEST_TABLE);

		assertEquals(numRows, numRowsAfterClearing);
		assertTrue(success);
		assertEquals(numRows + 1, numRowsAfterCreation);
		assertEquals("1", parser.id);
		assertEquals("petya", parser.name);
		assertEquals(1, parser.counter);

	}

	private int getNumRows(String dbTable) throws Exception
	{
		String getCountQuery = String.format("SELECT COUNT(*) FROM %1$s", dbTable);
		Statement st = connection.createStatement();
		ResultSet set = st.executeQuery(getCountQuery);
		set.next();
		return set.getInt(1);
	}

//	private void clearTable(String dbTable) throws Exception
//	{
//		String clearQuery = String.format("REMOVE FROM %1$s", dbTable);
//		Statement st = connection.createStatement();
//		st.executeUpdate(clearQuery);
//	}

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
