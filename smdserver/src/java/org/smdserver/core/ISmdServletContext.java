package org.smdserver.core;

import org.smdserver.users.IUsersStorage;
import org.smdserver.words.IWordsStorage;

public interface ISmdServletContext
{
	public IUsersStorage getUsersStorage ();
	public IWordsStorage getWordsStorage ();
	public void log (String message);
}
