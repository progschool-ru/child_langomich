package org.smdserver.core;

import java.io.PrintStream;
import java.util.ResourceBundle;
import org.smdserver.core.small.ConsoleSmdLogger;
import org.smdserver.core.small.ISmdLogger;
import org.smdserver.db.DbException;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.SmdDB;

class UTestCore implements ITestCore
{
	private static final String CONFIG_FILE = "org.smdserver.config";
	private static final String SERVER_PROPERTIES_FILE_KEY = "file.server.properties";
	private static final String LOCALE_FILE_KEY = "file.locale";
	private static final PrintStream LOG_STREAM = System.out;
	
	UTestFactory factory;
	
	public UTestCore() throws DbException
	{
		ISmdLogger logger = new ConsoleSmdLogger(LOG_STREAM);	

		ResourceBundle rb = ResourceBundle.getBundle(CONFIG_FILE);	
		String serverFile = rb.getString(SERVER_PROPERTIES_FILE_KEY);
		ResourceBundle serverRB = ResourceBundle.getBundle(serverFile);
		String localeFile = rb.getString(LOCALE_FILE_KEY);
		factory = new UTestFactory(rb, serverRB, localeFile, logger, null, localeFile);
				
		ISmdDB db = new SmdDB(factory.createDBConfig(), logger);
		factory.setDB(db);
	}
	
	public ITestFactory getFactory()
	{
		return factory;
	}
}
