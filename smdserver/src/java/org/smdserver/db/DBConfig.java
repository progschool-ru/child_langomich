package org.smdserver.db;

import java.util.ResourceBundle;
import org.smdserver.core.small.ResourceBundleBased;

public class DBConfig extends ResourceBundleBased implements IDBConfig
{	
	public DBConfig(String configFile, String serverConfigKey)
	{
		super(ResourceBundle.getBundle(
				     ResourceBundle.getBundle(configFile).getString(serverConfigKey)
			));
	}
	
	public DBConfig(ResourceBundle rb)
	{
		super(rb);
	}

	//IDBConfig implementation
	public String getTablesPrefix()
	{
		return getString("db.tablesPrefix");
	}
	
	public String getDBUrl()
	{
		return getString("db.url");
	}
	
	public String getDBUser()
	{
		return getString("db.user");
	}
	
	public String getDBPassword()
	{
		return getString("db.password");
	}
	
	//IMaintenanceConfig implementation
	public boolean isMaintenanceAllowed()
	{
		return getBoolean("maintenance.allowed");
	}
	
	public String getMaintenancePassword()
	{
		return getString("maintenance.password");
	}	
}
