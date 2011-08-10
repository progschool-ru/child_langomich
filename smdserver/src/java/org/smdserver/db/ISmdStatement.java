package org.smdserver.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ISmdStatement
{
	public int addQuery(String query);
	public void startSet(int queryNumber);
	public void addString(String param);
	public void addInteger(Integer param);
	public void addLong(Long param);
	public void addDouble(Double param);
	
	public int processQueries(Connection connection) throws SQLException;
	public List<ResultSet> select(Connection connection) throws SQLException;
	public void closeStatements() throws SQLException;
}
