package org.smdserver.actionssystem;

import org.smdserver.users.IUsersStorage;

class SmdServletContext implements ISmdServletContext
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
