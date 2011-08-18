package org.smdserver.core;

import java.util.ResourceBundle;
import org.smdserver.db.IDBConfig;
import org.smdserver.jsp.IJSPConfig;
import org.smdserver.maintenance.IMaintenanceConfig;

public interface IConfigProperties extends IJSPConfig, IDBConfig, IMaintenanceConfig
{
	@Deprecated
	ResourceBundle getConfigResource();
	String getWebCharset();
}
