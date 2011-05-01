package org.smdserver.core;

import org.smdserver.users.IUsersStorage;

public interface ISmdServletContext
{
	public IUsersStorage getUsersStorage();
}
