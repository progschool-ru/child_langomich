package org.smdserver.db;

public interface ISmdDB
{
	public boolean updateSingle(String query);
	public boolean selectSingle(String query, IResultParser parser);
	public String escapeString(String dirtyValue);

	public boolean isActive();
	public boolean close();
}
