package org.smdserver.core;

import org.smdserver.db.IDBConfig;
import org.smdserver.mail.IMailman;
import org.smdserver.util.ISmdLogger;
import org.smdserver.users.IUsersStorage;
import org.smdserver.words.IWordsStorage;

public interface ISmdServletContext extends ISmdLogger
{
	public IUsersStorage getUsersStorage ();
	public IWordsStorage getWordsStorage ();
	public ISmdLogger getLogger();
	public ISmdCoreFactory getFactory();
	@Deprecated
	public IDBConfig getDBConfig();
	@Deprecated
	public ICoreConfig getCoreConfig();
	public IMailman getMailman();
	public void log (String message);
}
