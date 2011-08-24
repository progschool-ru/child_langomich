package org.smdserver.core;

import org.smdserver.core.small.ICoreConfig;
import org.smdserver.auth.IRegisterConfig;
import org.smdserver.db.IDBConfig;
import org.smdserver.jsp.IJSPConfig;
import org.smdserver.mail.IMailman;
import org.smdserver.users.IUsersStorage;
import org.smdserver.words.IWordsStorage;

public interface ISmdCoreFactory 
{
	public IDBConfig createDBConfig();
	public IMailman createMailman();
	public IWordsStorage createWordsStorage();
	public IUsersStorage createUsersStorage();
	public IRegisterConfig createRegisterConfig();
	public IJSPConfig createJSPConfig();
	public ICoreConfig createCoreConfig();
}
