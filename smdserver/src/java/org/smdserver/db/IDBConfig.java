package org.smdserver.db;

public interface IDBConfig 
{
	public String getTablesPrefix();
	public String getDBUrl();
	public String getDBUser();
	public String getDBPassword();
}
