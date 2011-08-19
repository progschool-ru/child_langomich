package org.smdserver.core;

import java.io.PrintStream;
import javax.servlet.ServletContext;
import org.smdserver.db.DBConfig;
import org.smdserver.db.DbException;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.SmdDB;
import org.smdserver.jsp.IJSPConfig;
import org.smdserver.jsp.SmdUrl;
import org.smdserver.users.IUsersStorage;
import org.smdserver.users.UsersDBStorage;
import org.smdserver.util.ComplexSmdLogger;
import org.smdserver.util.ISmdLogger;
import org.smdserver.words.IWordsStorage;
import org.smdserver.words.WordsDBStorage;

class SmdCore implements ISmdCore
{
	private static final String CONFIG_PARAM = "config";
	private static final String SERVER_PROPERTIES_FILE_KEY = "server.properties.file";
	
	private static final PrintStream LOG_STREAM = System.out;
	
	private ConfigProperties configProperties;
	private DBConfig dbConfig;
	private ISmdLogger logger;
	private ISmdDB db;
	private String tablesPrefix;
	
	public SmdCore()
	{
	}

	@Override
	public void finalize() throws Throwable
	{
		if(db != null)
		{
			db.close();
		}
		super.finalize();
	}
	
	public void setContext (ServletContext context)
	{
		init(context);
	}
	
	public IJSPConfig getJSPConfig()
	{
		return configProperties;
	}
	
	public ISmdServletContext createContext()
	{
		return new SmdServletContext(createUsersStorage(), 
				                     createWordsStorage(), 
				                     dbConfig,
				                     logger);
	}
	
	private void init(ServletContext context)
	{
		logger = new ComplexSmdLogger(context, LOG_STREAM);	
		recreateConfigProperties(context);
		initDB(configProperties, logger);
		SmdUrl.initParams(configProperties, configProperties.getWebCharset(), logger);
	}
	
	private void recreateConfigProperties(ServletContext context)
	{
		closeIfNotNull(configProperties);
		String configFile = context.getInitParameter(CONFIG_PARAM);
		configProperties = new ConfigProperties(configFile, context.getContextPath());
		
		closeIfNotNull(dbConfig);
		dbConfig = new DBConfig(configFile, SERVER_PROPERTIES_FILE_KEY);
	}

	private boolean initDB(ConfigProperties config, ISmdLogger logger)
	{
		if(db != null)
		{
			db.close();
			db = null;
		}
			
		tablesPrefix = dbConfig.getTablesPrefix();
		try
		{
			db = new SmdDB(dbConfig, logger);
			return true;
		}
		catch (DbException e)
		{
			logger.log(e.getMessage());
			return false;
		}
	}	
	
	private IWordsStorage createWordsStorage()
	{
		if(db != null)
		{
			return new WordsDBStorage(db, tablesPrefix);
		}
		else
		{
			return null;
		}
	}
	
	private IUsersStorage createUsersStorage()
	{
		if(db != null)
		{
			return new UsersDBStorage(db, tablesPrefix);
		}
		else
		{
			return null;
		}
	}
	
	private void closeIfNotNull(IClosable object)
	{
		if(object != null)
		{
			object.close();
		}
	}
}
