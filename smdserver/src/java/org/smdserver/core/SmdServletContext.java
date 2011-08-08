package org.smdserver.core;

import org.smdserver.users.IUsersStorage;
import org.smdserver.words.IWordsStorage;

public class SmdServletContext implements ISmdServletContext
{
	private IUsersStorage usersStorage;
	private IWordsStorage wordsStorage;
	private ISmdLogger logger;
	private String configResourceName;

	public SmdServletContext (IUsersStorage usersStorage, 
								IWordsStorage wordsStorage,
								String configResourceName,
								ISmdLogger logger)
	{
		this.usersStorage = usersStorage;
		this.wordsStorage = wordsStorage;
		this.configResourceName = configResourceName;
		this.logger = logger;
	}
	
	public String getConfigResourceName()
	{
		return configResourceName;
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
