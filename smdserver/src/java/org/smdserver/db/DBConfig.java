package org.smdserver.db;

import java.util.ResourceBundle;
import org.smdserver.core.IClosable;

public class DBConfig implements IDBConfig, IClosable
{	
	private ResourceBundle serverRB;
	
	public DBConfig(String configFile, String serverConfigKey)
	{
		ResourceBundle rb = ResourceBundle.getBundle(configFile);	
		String serverFile = rb.getString(serverConfigKey);
		this.serverRB = ResourceBundle.getBundle(serverFile);
	}
	
	public boolean close()
	{
		this.serverRB = null;
		return true;
	}

	//IDBConfig implementation
	public String getTablesPrefix()
	{
		return serverRB.getString("db.tablesPrefix");
	}
	
	public String getDBUrl()
	{
		return serverRB.getString("db.url");
	}
	
	public String getDBUser()
	{
		return serverRB.getString("db.user");
	}
	
	public String getDBPassword()
	{
		return serverRB.getString("db.password");
	}
	
	//IMaintenanceConfig implementation
	public boolean isMaintenanceAllowed()
	{
		return "true".equals(serverRB.getString("maintenance.allowed"));
	}
	
	public String getMaintenancePassword()
	{
		return serverRB.getString("maintenance.password");
	}	
}
