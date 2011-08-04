package org.smdserver.db;

public interface ISmdDB
{
	public boolean updateSingle(String query)
										throws DbException;
	public boolean selectSingle(String query, IResultParser parser)
										throws DbException;
	public String escapeString(String dirtyValue);

	public boolean isActive();
	public boolean close();
}
