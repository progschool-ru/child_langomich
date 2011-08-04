package org.smdserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class SmdDB implements ISmdDB
{
	private ResourceBundle rb;
	private Connection connection;

	public SmdDB(ResourceBundle rb) throws DbException
	{
		this.rb = rb;

		checkConnection();
	}

	public boolean isActive()
	{
		return connection != null;
	}

	public boolean close()
	{
		boolean success = true;
		try
		{
			if(connection != null && !connection.isClosed())
			{
				connection.close();
			}
		}
		catch(SQLException e)
		{
			//TODO: (2.medium) use logger
			System.out.println("SmdDB can't close connection: " + e.getMessage());
			success = false;
		}
		connection = null;
		return success;
	}

	public synchronized boolean updateSingle(String dbQuery) throws DbException
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
							throws DbException
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

	private void checkConnection() throws DbException
	{
		try
		{
			if(connection != null && !connection.isClosed() && connection.isValid(0))
				return;
		}
		catch(SQLException e)
		{
			//TODO: (3.low) log
			System.out.println(e.getMessage());
			close();
		}

		String url = rb.getString("db.url");
		String user = rb.getString("db.user");
		String password = rb.getString("db.password");
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(url, user, password);
		}
		catch(Exception e)
		{
			//TODO: (3.low) log
			System.out.println(e.getMessage());
			DbException dbE = new DbException(DbException.CANT_CONNECT_TO_DATABASE);
			dbE.setReason(e);
			throw dbE;
		}
	}
}
