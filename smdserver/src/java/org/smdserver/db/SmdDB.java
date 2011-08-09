package org.smdserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;
import org.smdserver.core.ISmdLogger;

public class SmdDB implements ISmdDB
{
	private ResourceBundle rb;
	private Connection connection;
	private ISmdLogger logger;
	private final Object sync = new Object();

	public SmdDB(ResourceBundle rb, ISmdLogger logger) throws DbException
	{
		this.rb = rb;
		this.logger = logger;

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
	
	public int processSmdStatement(ISmdStatment statement) throws DbException
	{
		int updatedRows = 0;
		synchronized (sync)
		{
			checkConnection();
			if(!setAutoCommit(false))
			{
				return -1;
			}
			try
			{
				updatedRows = statement.processQueries(connection);
				connection.commit();
			}
			catch(SQLException e)
			{
				log("SmdDB.updateGroup: " +e.getMessage());
				updatedRows = -1;
				try
				{
					connection.rollback();
				}
				catch(SQLException ex)
				{
					log("SmdDB.processSmdStatement: rollbackError" +ex.getMessage());
					throw new DbException(DbException.ROLLBACK_ERROR, ex);
				}
			}
			
			if(!setAutoCommit(true))
			{
				closeWithoutSync();
			}
		}
		return updatedRows;
	}
	
	public int updateGroup (List<String> queries) throws DbException
	{
		int updatedRows = 0;
		synchronized (sync)
		{
			checkConnection();
			if(!setAutoCommit(false))
			{
				return -1;
			}

			int count = 0;

			try
			{
				Statement statement = connection.createStatement();
				for(String query : queries)
				{
					updatedRows += statement.executeUpdate(query);
					count ++;
				}
				statement.close();
				connection.commit();
			}
			catch(SQLException e)
			{
				log("SmdDB.updateGroup: " +e.getMessage());
				log(queries.get(count));
				updatedRows = -1;
				try
				{
					connection.rollback();
				}
				catch(SQLException ex)
				{
					log("SmdDB.updateGroup: rollbackError" +ex.getMessage());
					throw new DbException(DbException.ROLLBACK_ERROR, ex);
				}
			}

			if(!setAutoCommit(true))
			{
				closeWithoutSync();
			}
		}
		return updatedRows;
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
			log("SmdDB.updateSingle: " +e.getMessage());
			log(dbQuery);
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
			log("select: " +e.getMessage());
			log(dbQuery);
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
			log("SmdDB.checkConnection: " +e.getMessage());
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
			log("SmdDB.checkConnection: " +e.getMessage());
			throw new DbException(DbException.CANT_CONNECT_TO_DATABASE, e);
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
			log("SmdDB.setAutoCommit: " +e.getMessage());
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
			log("SmdDB can't close connection: " + e.getMessage());
			success = false;
		}
		connection = null;
		return success;
	}

	private void log(String message)
	{
		if(logger != null)
		{
			logger.log(message);
		}
	}
}
