package org.smdserver.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SmdStatement implements ISmdStatement
{
	private static final int STRING = 0;
	private static final int DOUBLE = 1;
	private static final int INTEGER = 2;
	private static final int LONG = 3;
	
	private static final int UPDATE = 0;
	private static final int SELECT = 1;

	private List<String> queries = new ArrayList<String>();
	
	private List<Integer> setToQuery = new ArrayList<Integer>();
	private List<List<Object>> params = new ArrayList<List<Object>>();
	private List<List<Integer>> types = new ArrayList<List<Integer>>();
	
	private List<Object> lastParams;
	private List<Integer> lastTypes;
	
	private int updatedRows;
	private List<ResultSet> resultSets;
	private PreparedStatement[] preparedStatements;

	@Override
	protected void finalize() throws Throwable
	{
		closeStatements();
		super.finalize();
	}
	
	public int addQuery(String query)
	{
		queries.add(query);
		return queries.size() - 1;
	}
	
	public void startSet(int queryNumber)
	{
		setToQuery.add(queryNumber);
		lastParams = new ArrayList<Object>();
		lastTypes = new ArrayList<Integer>();
		params.add(lastParams);
		types.add(lastTypes);
	}
	
	public void addString(String param)
	{
		lastParams.add(param);
		lastTypes.add(STRING);
	}

	public void addInteger(Integer param)
	{
		lastParams.add(param);
		lastTypes.add(INTEGER);
	}

	public void addLong(Long param)
	{
		lastParams.add(param);
		lastTypes.add(LONG);
	}
	
	public void addDouble(Double param)
	{
		lastParams.add(param);
		lastTypes.add(DOUBLE);
	}
	
	public int processQueries(Connection connection) throws SQLException
	{
		updatedRows = 0;
		processQueries(connection, UPDATE);
		return updatedRows;
	}
	
	public List<ResultSet> select(Connection connection) throws SQLException
	{
		resultSets = new ArrayList<ResultSet>();
		processQueries(connection, SELECT);
		return resultSets;
	}
	
	public void closeStatements() throws SQLException
	{
		if(preparedStatements == null)
		{
			return;
		}
		
		SQLException lastException = null;
		for(int i = 0; i < preparedStatements.length; i++)
		{
			if(preparedStatements[i] != null)
			{
				try
				{
					preparedStatements[i].close();
				}
				catch(SQLException e)
				{
					lastException = e;
				}
			}
		}
		
		preparedStatements = null;
		
		if(lastException != null)
		{
			throw lastException;
		}
	}
	
	private int processQueries(Connection connection, int type) throws SQLException
	{
		closeStatements();
		preparedStatements = new PreparedStatement[queries.size()];
		
		for(int setNumber = 0; setNumber < setToQuery.size(); setNumber ++)
		{
			int queryNumber = setToQuery.get(setNumber);
			if(queryNumber >= queries.size())
				continue;

			PreparedStatement statement = getStatement(preparedStatements, queryNumber, connection);
			fillStatement(statement, setNumber);
			doStatement(statement, type);
		}

		return updatedRows;		
	}
	
	private void doStatement(PreparedStatement statement, int type) throws SQLException
	{
		switch (type)
		{
			case UPDATE:
			{
				updatedRows += statement.executeUpdate();
				break;
			}
			case SELECT:
			{
				resultSets.add(statement.executeQuery());
				break;
			}
		}		
	}
		
	private PreparedStatement getStatement(PreparedStatement[] statements, 
			                               int queryNumber,
				                           Connection connection)
			                          throws SQLException
	{
		if(queryNumber >= queries.size())
			return null;
		
		if(statements[queryNumber] == null)
		{
			String query = queries.get(queryNumber);
			statements[queryNumber] = connection.prepareStatement(query);
		}
		
		return statements[queryNumber];
	}
	
	private void setStatementParam(int index, Object param, int type, 
			                    PreparedStatement statement) throws SQLException
	{
		switch(type)
		{
			case STRING:
			{
				statement.setString(index, (String)param);
				break;
			}
			case INTEGER:
			{
				statement.setInt(index, (Integer)param);
				break;
			}
			case DOUBLE:
			{
				statement.setDouble(index, (Double)param);
				break;
			}
			case LONG:
			{
				statement.setLong(index, (Long)param);
			}
		}
		
	}
	
	private void fillStatement(PreparedStatement statement, int setNumber) 
			                     throws SQLException
	{
		List<Object> currentParams = params.get(setNumber);
		List<Integer> currentTypes = types.get(setNumber);
		
		statement.clearParameters();
		
		for(int i = 0; i < currentTypes.size(); i++)
		{
			Object param = currentParams.get(i);
			Integer type = currentTypes.get(i);
			setStatementParam(i+1, param, type, statement);
		}
	}
}
