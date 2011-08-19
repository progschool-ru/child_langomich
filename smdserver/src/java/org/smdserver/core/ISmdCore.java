package org.smdserver.core;

import org.smdserver.db.IDBConfig;
import org.smdserver.jsp.IJSPConfig;
import org.smdserver.users.IUsersStorage;
import org.smdserver.words.IWordsStorage;

public interface ISmdCore 
{
	public IJSPConfig getJSPConfig();
	
	public ISmdServletContext createContext();
	public IWordsStorage createWordsStorage();
	public IUsersStorage createUsersStorage();
	public IDBConfig getDBConfig();
}
