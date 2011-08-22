package org.smdserver.db;

import java.util.List;

public interface ISmdDB
{
	public int updateGroup (List<String> queries) throws DbException;
	public boolean updateSingle(String query) throws DbException;
	public int select(String dbQuery, IMultipleResultParser parser) throws DbException;
	public boolean selectSingle(String query, IResultParser parser)	throws DbException;
	public String escapeString(String dirtyValue);
	
	public int processSmdStatement(ISmdStatement statement) throws DbException;
	public boolean selectSingle(ISmdStatement st, IResultParser parser) throws DbException;
	public int select(ISmdStatement st, IMultipleResultParser parser) throws DbException;
	
	public String getTablesPrefix();

	public boolean isActive();
	public boolean close();
}
