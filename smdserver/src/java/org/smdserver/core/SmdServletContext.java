package org.smdserver.core;

import org.smdserver.db.IDBConfig;
import org.smdserver.mail.IMailman;
import org.smdserver.util.ISmdLogger;
import org.smdserver.users.IUsersStorage;
import org.smdserver.words.IWordsStorage;

class SmdServletContext implements ISmdServletContext
{
	private IUsersStorage usersStorage;
	private IWordsStorage wordsStorage;
	private IDBConfig dbConfig;
	private IMailman mailman;
	
	private ISmdCoreFactory factory;
	private ISmdLogger logger;

	public SmdServletContext (ISmdCoreFactory factory, ISmdLogger logger)
	{
		this.logger = logger;
		this.factory = factory;
	}

	public IDBConfig getDBConfig()
	{
		if(dbConfig == null)
		{
			dbConfig = factory.createDBConfig();
		}
		return dbConfig;
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

	public void log (String message)
	{
		if(logger != null)
		{
			logger.log(message);
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
