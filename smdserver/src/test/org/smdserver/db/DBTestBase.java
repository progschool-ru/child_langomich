package org.smdserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import org.junit.After;
import org.junit.Before;

public class DBTestBase 
{
	private static final String TEST_TABLE = "test1";

	private Connection connection;
	private String testTable;

	@Before
    public void setUp () throws Exception
	{
		String testConfig = ResourceBundle.getBundle("org.smdserver.config")
				                      .getString("server.test.properties.file");
		ResourceBundle rb = ResourceBundle.getBundle(testConfig);

		String url = rb.getString("db.url");
		String user = rb.getString("db.user");
		String password = rb.getString("db.password");
		connection = DriverManager.getConnection(url, user, password);
		testTable = rb.getString("db.tablesPrefix") + TEST_TABLE;
		String clearQuery = String.format("DELETE FROM %1$s;", testTable);
		Statement st = connection.createStatement();
		st.executeUpdate(clearQuery);
		setUpChild(rb);
    }

	@After
	public void tearDown() throws Exception
	{
		if(!connection.isClosed())
		{
			String clearQuery = String.format("DELETE FROM %1$s;", testTable);
			Statement st = connection.createStatement();
			st.executeUpdate(clearQuery);
			st.close();
		
			connection.close();
		}
		connection = null;
		tearDownChild();
	}
	
	protected void setUpChild(ResourceBundle rb) throws Exception {}
	protected void tearDownChild() throws Exception {}
	
	protected Connection getConnection()
	{
		return connection;
	}
	
	protected String getTestTable()
	{
		return testTable;
	}
	
	protected class TestParser implements IResultParser
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
	
	protected int getNumRows() throws Exception
	{
		String getCountQuery = String.format("SELECT COUNT(*) FROM %1$s", testTable);
		Statement st = connection.createStatement();
		ResultSet set = st.executeQuery(getCountQuery);
		set.next();
		int numRows = set.getInt(1);
		st.close();
		return numRows;
	}	
}