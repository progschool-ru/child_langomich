package org.smdserver.core;

import javax.servlet.ServletContext;
import org.smdserver.users.IUsersStorage;
import org.smdserver.words.IWordsStorage;

public class SmdServletContext implements ISmdServletContext
{
	private IUsersStorage usersStorage;
	private IWordsStorage wordsStorage;
	private ServletContext servletContext;
	private ISmdConfig config;

	public SmdServletContext (IUsersStorage usersStorage, 
								IWordsStorage wordsStorage,
								ServletContext servletContext)
	{
		this.usersStorage = usersStorage;
		this.wordsStorage = wordsStorage;
		this.servletContext = servletContext;
	}

	public IUsersStorage getUsersStorage ()
	{
		return usersStorage;
	}

	public IWordsStorage getWordsStorage ()
	{
		return wordsStorage;
	}

	public void log (String message)
	{
		servletContext.log(message);
	}

	public ISmdConfig getConfig()
	{
		return SmdConfigBean.getInstance();
	}
}
