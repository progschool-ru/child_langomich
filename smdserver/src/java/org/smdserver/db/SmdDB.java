package org.smdserver.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SmdDB implements ISmdDB
{
	private Connection connection;

	public SmdDB(Connection connection)
	{
		this.connection = connection;
	}

//	public void close() throws SQLException
//	{
//		this.connection.close();
//	}

	public synchronized boolean updateSingle(String dbQuery)
	{
		int countRows = 0;
		try
		{
			checkConnection();
			Statement statement = connection.createStatement();
			countRows = statement.executeUpdate(dbQuery);
			statement.close();
		}
		catch(SQLException e)
		{
			//TODO (3.low) log error
			System.out.println(e.getMessage());
			return false;
		}
		return countRows == 1;
	}
	
	public boolean selectSingle(String dbQuery, IResultParser parser)
	{
		if(parser == null)
			return false;
		
		boolean success = false;
		try
		{
			checkConnection();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(dbQuery);
			if(result.next())
			{
				success = parser.parse(result);
			}
			statement.close();
		}
		catch(SQLException e)
		{
			//TODO (3.low) log error
			success = false;
		}
		return false;
	}
	public String escapeString(String dirtyValue)
	{
		return dirtyValue.replaceAll("([\\\\\"])", "\\\\$1");
	}

	private void checkConnection()
	{
		
	}
}
