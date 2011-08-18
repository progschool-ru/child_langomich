package org.smdserver.core;

import org.smdserver.util.ISmdLogger;
import org.smdserver.users.IUsersStorage;
import org.smdserver.words.IWordsStorage;

public interface ISmdServletContext extends ISmdLogger
{
	public IUsersStorage getUsersStorage ();
	public IWordsStorage getWordsStorage ();
	public ISmdLogger getLogger();
	public String getConfigResourceName();
	public void log (String message);
}
