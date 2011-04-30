package org.smdserver.actionssystem;

import org.smdserver.users.IUsersStorage;

public interface ISmdServletContext
{
	public IUsersStorage getUsersStorage();
}
