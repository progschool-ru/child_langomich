package org.smdserver.core;

import java.util.ResourceBundle;
import org.smdserver.util.ISmdLogger;
import org.smdserver.users.IUsersStorage;
import org.smdserver.words.IWordsStorage;

public interface ISmdServletContext extends ISmdLogger
{
	public IUsersStorage getUsersStorage ();
	public IWordsStorage getWordsStorage ();
	public ISmdLogger getLogger();
	@Deprecated
	public ResourceBundle getConfigResource();
	public void log (String message);
}
