package org.smdserver.core;

import org.smdserver.core.small.ICoreConfig;
import org.smdserver.db.IDBConfig;
import org.smdserver.mail.IMailman;
import org.smdserver.core.small.ISmdLogger;
import org.smdserver.users.IUsersStorage;
import org.smdserver.words.IWordsStorage;

public interface ISmdServletContext extends ISmdLogger //TODO: (3.low) remove this extention
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
	@Deprecated
	public void log (String message);
	@Deprecated
	public void log (Throwable e);
}
