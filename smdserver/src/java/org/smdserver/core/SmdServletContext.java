package org.smdserver.core;

import org.smdserver.util.ISmdLogger;
import org.smdserver.users.IUsersStorage;
import org.smdserver.words.IWordsStorage;

public class SmdServletContext implements ISmdServletContext
{
	private IUsersStorage usersStorage;
	private IWordsStorage wordsStorage;
	private ISmdLogger logger;
	private IConfigProperties config;

	public SmdServletContext (IUsersStorage usersStorage, 
								IWordsStorage wordsStorage,
								IConfigProperties config,
								ISmdLogger logger)
	{
		this.usersStorage = usersStorage;
		this.wordsStorage = wordsStorage;
		this.logger = logger;
		this.config = config;
	}

	public IConfigProperties getConfig()
	{
		return config;
	}

	public IUsersStorage getUsersStorage ()
	{
		return usersStorage;
	}

	public IWordsStorage getWordsStorage ()
	{
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
}
