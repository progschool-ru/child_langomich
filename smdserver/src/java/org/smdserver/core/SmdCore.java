package org.smdserver.core;

import java.io.PrintStream;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;
import org.smdserver.db.DbException;
import org.smdserver.db.IDBConfig;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.SmdDB;
import org.smdserver.jsp.SmdUrl;
import org.smdserver.util.ComplexSmdLogger;
import org.smdserver.util.ISmdLogger;

class SmdCore implements ISmdCore
{
	private static final String CONFIG_PARAM = "config";
	private static final String SERVER_PROPERTIES_FILE_KEY = "server.properties.file";
	
	private static final PrintStream LOG_STREAM = System.out;
	
	private ISmdCoreFactory factory;
	
	private ISmdLogger logger;
	private ISmdDB db;
	
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
	
	public void setContext (ServletContext context)//TODO: (2.medium) Что делать с разными сервлетами, с разными контекстками?
	{
		if(factory == null)
		{
			init(context);
		}
	}
	
	public ISmdCoreFactory getFactory()
	{
		return factory;
	}
	
	public ISmdServletContext createContext()
	{
		return new SmdServletContext(factory, logger);
	}
	
	private void init(ServletContext context)
	{
		logger = new ComplexSmdLogger(context, LOG_STREAM);	

		String configFile = context.getInitParameter(CONFIG_PARAM);
		ResourceBundle rb = ResourceBundle.getBundle(configFile);	
		String serverFile = rb.getString(SERVER_PROPERTIES_FILE_KEY);
		ResourceBundle serverRB = ResourceBundle.getBundle(serverFile);
		SmdCoreFactory f = new SmdCoreFactory(rb, serverRB, logger, 
				                              null, context.getContextPath());
		
		initDB(f.createDBConfig(), logger);
		
		f.setDB(db);
		factory = f;
		
		SmdUrl.initParams(f.createJSPConfig(), logger);

	}

	private boolean initDB(IDBConfig dbConfig, ISmdLogger logger)
	{
		if(db != null)
		{
			db.close();
			db = null;
		}
		
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
}
