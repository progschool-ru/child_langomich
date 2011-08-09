package org.smdserver.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Test;
import static org.junit.Assert.*;

public class SmdStatementTest extends DBTestBase
{
	@Test
	public void testUpdateAndSelectSingle() throws Exception
	{
		int numRows = getNumRows();
		
		String query = String.format("INSERT INTO %1$s (test_id, name) VALUE (?, ?);", getTestTable());
		SmdStatement statement = new SmdStatement();
		int queryIndex = statement.addQuery(query);
		statement.startSet(queryIndex);
		statement.addString("1");
		statement.addString("petya");		
		int updatedRows = statement.processQueries(getConnection());
		statement.closeStatements();
		
		int numRowsAfterCreation = getNumRows();

		String selectQuery = String.format("SELECT * FROM %1$s WHERE test_id = ?;", getTestTable());
		statement = new SmdStatement();
		statement.addQuery(selectQuery);
		statement.startSet(0);
		statement.addString("1");		
		ResultSet set = statement.select(getConnection()).get(0);
		TestParser parser = new TestParser();
		set.next();
		parser.parse(set);
		statement.closeStatements();

		String clearQuery = String.format("DELETE FROM %1$s WHERE test_id=\"1\";", getTestTable());
		Statement st = getConnection().createStatement();
		st.executeUpdate(clearQuery);
		st.close();
		int numRowsAfterClearing = getNumRows();

		assertEquals(numRows, numRowsAfterClearing);
		assertEquals(1, updatedRows);
		assertEquals(numRows + 1, numRowsAfterCreation);
		assertEquals("1", parser.id);
		assertEquals("petya", parser.name);
		assertEquals(1, parser.counter);
	}


	@Test
	public void testIncorrectQuery() throws Exception
	{
		int numRows = getNumRows();

		String query = String.format("INSERT INT %1$s (test_id, name) VALUE (?, ?);", getTestTable());
		SmdStatement st = new SmdStatement();
		st.addQuery(query);
		st.startSet(0);
		st.addString("1");
		st.addString("petya");
		boolean success = true;
		try
		{
			st.processQueries(getConnection());
		}
		catch(SQLException e)
		{
			success = false;
		}
		int numRows2 = getNumRows();

		assertFalse(success);
		assertEquals(numRows, numRows2);
	}
}
