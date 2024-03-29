package org.smdserver.core;

import org.smdserver.core.small.ICoreConfig;
import org.smdserver.db.IDBConfig;
import org.smdserver.mail.IMailman;
import org.smdserver.core.small.ISmdLogger;
import org.smdserver.users.IUsersStorage;
import org.smdserver.words.IWordsStorage;

class USmdServletContext implements ISmdServletContext
{
	private IUsersStorage usersStorage;
	private IWordsStorage wordsStorage;
	private IDBConfig dbConfig;
	private ICoreConfig coreConfig;
	private IMailman mailman;
	
	private ISmdCoreFactory factory;
	private ISmdLogger logger;

	public USmdServletContext (ISmdCoreFactory factory, ISmdLogger logger)
	{
		this.logger = logger;
		this.factory = factory;
	}
	
	public ISmdCoreFactory getFactory()
	{
		return factory;
	}

	@Deprecated
	public IDBConfig getDBConfig()
	{
		if(dbConfig == null)
		{
			dbConfig = factory.createDBConfig();
		}
		return dbConfig;
	}
	
	@Deprecated
	public ICoreConfig getCoreConfig()
	{
		if(coreConfig == null)
		{
			coreConfig = factory.createCoreConfig();
		}
		return coreConfig;
	}

	public IUsersStorage getUsersStorage ()
	{
		if(usersStorage == null)
		{
			usersStorage = factory.createUsersStorage();
		}
		return usersStorage;
	}

	public IWordsStorage getWordsStorage ()
	{
		if(wordsStorage == null)
		{
			wordsStorage = factory.createWordsStorage();
		}
		return wordsStorage;
	}

	public ISmdLogger getLogger()
	{
		return logger;
	}

	@Deprecated
	public void log (String message)
	{
		if(logger != null)
		{
			logger.log(message);
		}
	}
	
	@Deprecated
	public void log (Throwable e)
	{
		if(logger != null)
		{
			logger.log(e);
		}
	}
	
	public IMailman getMailman()
	{
		if(mailman == null)
		{
			mailman = factory.createMailman();
		}
		return mailman;
	}
}
