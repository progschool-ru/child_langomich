package org.smdserver.core;

import java.io.PrintStream;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;
import org.smdserver.db.SmdDB;
import org.smdserver.jsp.IJSPConfig;
import org.smdserver.jsp.SmdUrl;
import org.smdserver.util.ComplexSmdLogger;
import org.smdserver.util.ISmdLogger;

class SmdCore implements ISmdCore
{
	private static final String CONFIG_PARAM = "config";
	private static final String SERVER_PROPERTIES_FILE_KEY = "server.properties.file";
	
	private static final PrintStream LOG_STREAM = System.out;
	
	private ConfigProperties configProperties;
	private ISmdLogger logger;
	
	public SmdCore()
	{
	}
	
	public void setContext (ServletContext context)
	{
		init(context);
	}
	
	public IJSPConfig getJSPConfig()
	{
		return configProperties;
	}
	
	@Deprecated
	public ResourceBundle getConfigResource()
	{
		return configProperties.getConfigResource();
	}
	
	private void init(ServletContext context)
	{
		logger = new ComplexSmdLogger(context, LOG_STREAM);	
		recreateConfigProperties(context);
		SmdUrl.initParams(configProperties, configProperties.getWebCharset(), logger);
	}
	
	private void recreateConfigProperties(ServletContext context)
	{
		if(configProperties != null)
		{
			configProperties.close();
		}
		String configFile = context.getInitParameter(CONFIG_PARAM);
		configProperties = new ConfigProperties(configFile, 
				                                SERVER_PROPERTIES_FILE_KEY,
				                                context.getContextPath());
	}
}
