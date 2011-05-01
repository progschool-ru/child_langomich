package org.smdserver.core;

import org.smdserver.users.IUsersStorage;

public class SmdServletContext implements ISmdServletContext
{
	private IUsersStorage usersStorage;

	public SmdServletContext(IUsersStorage usersStorage)
	{
		this.usersStorage = usersStorage;
	}

	public IUsersStorage getUsersStorage()
	{
		return usersStorage;
	}
}
