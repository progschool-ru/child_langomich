package org.smdserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

public class SmdDB implements ISmdDB
{
	private ResourceBundle rb;
	private Connection connection;
	private final Object sync = new Object();

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
		synchronized(sync)
		{
			return closeWithoutSync();
		}
	}

	public int updateGroup (List<String> queries) throws DbException
	{
		int count = 0;
		synchronized (sync)
		{
			checkConnection();
			if(!setAutoCommit(false))
			{
				return -1;
			}

			try
			{
				Statement statement = connection.createStatement();
				for(String query : queries)
				{
					count += statement.executeUpdate(query);
				}
				statement.close();
				connection.commit();
			}
			catch(SQLException e)
			{
				//TODO: (2.medium) use logger
				System.out.println(e.getMessage());
				count = -1;
			}

			if(!setAutoCommit(true))
			{
				closeWithoutSync();
			}
		}
		return count;
	}

	public boolean updateSingle(String dbQuery) throws DbException
	{
		int countRows = 0;
		try
		{
			synchronized(sync)
			{
				checkConnection();
				Statement statement = connection.createStatement();
				countRows = statement.executeUpdate(dbQuery);
				statement.close();
			}
		}
		catch(SQLException e)
		{
			//TODO (3.low) log error
			System.out.println(e.getMessage());
			return false;
		}
		return countRows == 1;
	}

	public int select(String dbQuery, IMultipleResultParser parser) throws DbException
	{
		if(parser == null)
			return -1;

		int count = 0;
		try
		{
			synchronized(sync)
			{
				checkConnection();
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(dbQuery);
				count = parser.parse(result);
				statement.close();
			}
		}
		catch(SQLException e)
		{
			//TODO (3.low) log error
			count = -1;
		}
		return count;
	}

	public boolean selectSingle(String dbQuery, IResultParser parser)
							throws DbException
	{
		if(parser == null)
			return false;
		
		SingleToMultipleParserConverter p = new SingleToMultipleParserConverter(parser);
		return select(dbQuery, p) == 1;
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
			closeWithoutSync();
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

	private boolean setAutoCommit(boolean value)
	{
		try
		{
			connection.setAutoCommit(value);
			return true;
		}
		catch(SQLException e)
		{
			//TODO: (3.low) log
			System.out.println(e.getMessage());
			return false;
		}
	}

	private boolean closeWithoutSync()
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
}
