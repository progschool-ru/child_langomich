package org.smdserver.core;

import java.util.ResourceBundle;
import org.smdserver.db.DBConfig;
import org.smdserver.db.IDBConfig;
import org.smdserver.db.ISmdDB;
import org.smdserver.jsp.IJSPConfig;
import org.smdserver.jsp.JSPConfig;
import org.smdserver.mail.IMailConfig;
import org.smdserver.mail.IMailman;
import org.smdserver.mail.MailConfig;
import org.smdserver.mail.Mailman;
import org.smdserver.users.IUsersStorage;
import org.smdserver.users.UsersDBStorage;
import org.smdserver.util.ISmdLogger;
import org.smdserver.words.IWordsStorage;
import org.smdserver.words.WordsDBStorage;

class SmdCoreFactory implements ISmdCoreFactory
{
	private ResourceBundle rb;
	private ResourceBundle serverRB;
	private ISmdLogger logger;
	private ISmdDB db;
	private String basePath;
	
	public SmdCoreFactory (ResourceBundle rb, ResourceBundle serverRB, 
			               ISmdLogger logger, ISmdDB db, String basePath)
	{
		this.rb = rb;
		this.serverRB = serverRB;
		this.logger = logger;
		this.db = db;
		this.basePath = basePath;
	}
	
	public IDBConfig createDBConfig()
	{
		return new DBConfig(serverRB);
	}
	
	public IMailman createMailman()
	{
		return new Mailman(createMailConfig(), logger);
	}
	
	
	public IWordsStorage createWordsStorage()
	{
		if(db != null)
		{
			return new WordsDBStorage(db);
		}
		else
		{
			return null;
		}
	}
	
	public IUsersStorage createUsersStorage()
	{
		if(db != null)
		{
			return new UsersDBStorage(db);
		}
		else
		{
			return null;
		}
	}
	
	public IJSPConfig createJSPConfig()
	{
		return new JSPConfig(rb, basePath);
	}
	
	void setDB(ISmdDB db)
	{
		this.db = db;
	}

	private IMailConfig createMailConfig()
	{
		return new MailConfig(serverRB);
	}
}
