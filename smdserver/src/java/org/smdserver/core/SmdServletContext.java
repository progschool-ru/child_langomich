package org.smdserver.core;

import java.util.ResourceBundle;
import org.smdserver.util.ISmdLogger;
import org.smdserver.users.IUsersStorage;
import org.smdserver.words.IWordsStorage;

public class SmdServletContext implements ISmdServletContext
{
	private IUsersStorage usersStorage;
	private IWordsStorage wordsStorage;
	private ISmdLogger logger;
	private ResourceBundle configResource;

	public SmdServletContext (IUsersStorage usersStorage, 
								IWordsStorage wordsStorage,
								ResourceBundle configResource,
								ISmdLogger logger)
	{
		this.usersStorage = usersStorage;
		this.wordsStorage = wordsStorage;
		this.configResource = configResource;
		this.logger = logger;
	}
	
	@Deprecated
	public ResourceBundle getConfigResource()
	{
		return configResource;
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
