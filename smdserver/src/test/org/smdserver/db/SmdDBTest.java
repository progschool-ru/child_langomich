package org.smdserver.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Test;
import org.smdserver.core.small.ConsoleSmdLogger;
import static org.junit.Assert.*;

public class SmdDBTest extends DBTestBase
{
	private ISmdDB db;

	@Override
	protected void setUpChild(IDBConfig config) throws Exception
	{

		db = new SmdDB(config, new ConsoleSmdLogger(System.out));
	}
	
	@Override
	protected void tearDownChild() throws Exception
	{
		db.close();
		db = null;
	}

	@Test
	public void testItSelf () throws Exception
	{
		boolean first = getConnection().isClosed();

		Statement st = getConnection().createStatement();
		ResultSet set = st.executeQuery("SELECT 1");
		set.next();
		int i = set.getInt(1);
		boolean isAnythingElse = set.next();

		getConnection().close();
		boolean second = getConnection().isClosed();
		
		assertFalse(first);
		assertEquals(1, i);
		assertFalse(isAnythingElse);
		assertTrue(second);
	}

	@Test
	public void testUpdateSelectSingle() throws Exception
	{
		int numRows = getNumRows();

		String query = String.format("INSERT INTO %1$s (test_id, name) VALUE (\"1\", \"petya\");", getTestTable());
		boolean success = db.updateSingle(query);

		int numRowsAfterCreation = getNumRows();

		String selectQuery = String.format("SELECT * FROM %1$s WHERE test_id=\"1\";", getTestTable());
		TestParser parser = new TestParser();
		db.selectSingle(selectQuery, parser);

		String clearQuery = String.format("DELETE FROM %1$s WHERE test_id=\"1\";", getTestTable());
		Statement st = getConnection().createStatement();
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
	public void testPreparedUpdate() throws Exception
	{
		int numRows = getNumRows();
		SmdStatement st = new SmdStatement();

		String query1 = String.format("INSERT INTO %1$s (test_id, name) VALUE (?, ?);", getTestTable());
		int queryIndex1 = st.addQuery(query1);
		st.startSet(queryIndex1);
		st.addString("1");
		st.addString("petya");
		st.startSet(queryIndex1);
		st.addString("2");
		st.addString("vasua");
		st.closeStatements();
		
		String query2 = String.format("UPDATE %1$s SET name=? WHERE test_id = ?;", getTestTable());
		int queryIndex2 = st.addQuery(query2);
		st.startSet(queryIndex2);
		st.addString("kolya");
		st.addString("2");
		st.startSet(queryIndex1);
		st.addString("3");
		st.addString("misha");
		
		int updatedNumber = st.processQueries(getConnection());

		int numRowsAfterProcessing = getNumRows();

		String selectQuery = String.format("SELECT * FROM %1$s WHERE test_id=\"2\";", getTestTable());
		
		TestParser parser = new TestParser();
		db.selectSingle(selectQuery, parser);

		assertEquals(4, updatedNumber);
		assertEquals(numRows + 3, numRowsAfterProcessing);
		assertEquals("2", parser.id);
		assertEquals("kolya", parser.name);
		assertEquals(1, parser.counter);
	}
	
	@Test
	public void testNullParser() throws Exception
	{
		Statement st = getConnection().createStatement();

		String query = String.format("INSERT INTO %1$s (test_id, name) VALUE (\"1\", \"petya\");", getTestTable());
		st.executeUpdate(query);

		boolean caught = false;
		boolean success = true;
		try
		{
			String selectQuery = String.format("SELECT * FROM %1$s WHERE test_id=\"1\";", getTestTable());
			success = db.selectSingle(selectQuery, null);
		}
		catch(NullPointerException e)
		{
			caught = true;
		}

		String clearQuery = String.format("DELETE FROM %1$s WHERE test_id=\"1\";", getTestTable());
		st.executeUpdate(clearQuery);
		st.close();

		assertFalse(caught);
		assertFalse(success);
	}

	@Test
	public void testIncorrectQuery() throws Exception
	{
		int numRows = getNumRows();

		String query = String.format("INSERT INT %1$s (test_id, name) VALUE (\"1\", \"petya\");", getTestTable());
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

	@Test
	public void testCheckConnection () throws Exception
	{
		boolean first = db.isActive();

		SimpleParser parser = new SimpleParser();
		db.selectSingle("SELECT 1", parser);

		int number1 = parser.number;
		parser.number = 0;

		db.close();
		boolean second = db.isActive();

		db.selectSingle("SELECT 1", parser);
		int number2 = parser.number;
		boolean third = db.isActive();

		assertTrue(first);
		assertFalse(second);
		assertTrue(third);
		assertEquals(1, number1);
		assertEquals(1, number2);
	}

	private class SimpleParser implements IResultParser
	{
		int number;
		public boolean parse(ResultSet result) throws SQLException
		{
			number = result.getInt(1);
			return true;
		}
	}
}
