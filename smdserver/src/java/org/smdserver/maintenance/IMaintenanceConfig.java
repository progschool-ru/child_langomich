package org.smdserver.maintenance;

public interface IMaintenanceConfig 
{
	public boolean isMaintenanceAllowed();
	public String getMaintenancePassword();
}
