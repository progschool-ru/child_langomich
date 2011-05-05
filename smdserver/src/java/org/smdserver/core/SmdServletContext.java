package org.smdserver.core;

import org.smdserver.users.IUsersStorage;
import org.smdserver.words.IWordsStorage;

public class SmdServletContext implements ISmdServletContext
{
	private IUsersStorage usersStorage;
	private IWordsStorage wordsStorage;

	public SmdServletContext (IUsersStorage usersStorage, IWordsStorage wordsStorage)
	{
		this.usersStorage = usersStorage;
		this.wordsStorage = wordsStorage;
	}

	public IUsersStorage getUsersStorage ()
	{
		return usersStorage;
	}

	public IWordsStorage getWordsStorage ()
	{
		return wordsStorage;
	}
}
