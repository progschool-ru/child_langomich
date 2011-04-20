package org.smdserver.actionssystem;

import org.smdserver.users.UsersStorage;

class SmdServletContext implements ISmdServletContext
{
	private UsersStorage usersStorage;

	public SmdServletContext(UsersStorage usersStorage)
	{
		this.usersStorage = usersStorage;
	}

	public UsersStorage getUsersStorage()
	{
		return usersStorage;
	}
}
