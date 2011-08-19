package org.smdserver.db;

import org.smdserver.maintenance.IMaintenanceConfig;

public interface IDBConfig extends IMaintenanceConfig
{
	public String getTablesPrefix();
	public String getDBUrl();
	public String getDBUser();
	public String getDBPassword();
}
