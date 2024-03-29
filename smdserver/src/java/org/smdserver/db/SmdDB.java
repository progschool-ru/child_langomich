package org.smdserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.smdserver.core.small.ISmdLogger;

public class SmdDB implements ISmdDB
{
	private IDBConfig config;
	private Connection connection;
	private ISmdLogger logger;
	private final Object sync = new Object();

	public SmdDB(IDBConfig config, ISmdLogger logger) throws DbException
	{
		this.config = config;
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
	
	public String getTablesPrefix()
	{
		return config.getTablesPrefix();
	}
	
	public int processSmdStatement(ISmdStatement statement) throws DbException
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
				statement.closeStatements();
				connection.commit();
			}
			catch(SQLException e)
			{
				log(e);
				updatedRows = -1;
				try
				{
					connection.rollback();
				}
				catch(SQLException ex)
				{
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
				log(e);
				log("Queries count: " + queries.get(count));
				updatedRows = -1;
				try
				{
					connection.rollback();
				}
				catch(SQLException ex)
				{
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
			log(e);
			log(dbQuery);
			return false;
		}
		return countRows == 1;
	}

	public int select(ISmdStatement st, IMultipleResultParser parser) throws DbException
	{
		if(parser == null)
			return -1;		
		
		int count = 0;
		try
		{
			synchronized(sync)
			{	
				checkConnection();
				List<ResultSet> list = st.select(connection);
				if(list.size() > 0)
				{
					ResultSet result = list.get(0);
					count = parser.parse(result);
				}
				else
				{
					count = -1;
				}
				st.closeStatements();
			}
		}
		catch(SQLException e)
		{
			log(e);
			count = -1;
		}
		return count;
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
			log(e);
			log(dbQuery);
			count = -1;
		}
		return count;
	}

	public boolean selectSingle(ISmdStatement st, IResultParser parser)
							throws DbException
	{
		if(parser == null)
			return false;
		USingleToMultipleParserConverter p = new USingleToMultipleParserConverter(parser);
		return select(st, p) == 1;
	}
	
	public boolean selectSingle(String dbQuery, IResultParser parser)
							throws DbException
	{
		if(parser == null)
			return false;
		
		USingleToMultipleParserConverter p = new USingleToMultipleParserConverter(parser);
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
			log(e);
			closeWithoutSync();
		}

		String url = config.getDBUrl();
		String user = config.getDBUser();
		String password = config.getDBPassword();
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(url, user, password);
		}
		catch(Exception e)
		{
			throw new DbException(DbException.CANT_CONNECT_TO_DATABASE, e, DbException.CANT_CONNECT_TO_DATABASE);
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
			log(e);
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
			log("SmdDB can't close connection: ");
			log(e);
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

	private void log(Throwable e)
	{
		if(logger != null)
		{
			logger.log(e);
		}
	}
}
