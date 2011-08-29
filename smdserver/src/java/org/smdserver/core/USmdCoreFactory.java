package org.smdserver.core;

import java.util.Locale;
import org.smdserver.core.small.ICoreConfig;
import java.util.ResourceBundle;
import org.smdserver.auth.AuthLocale;
import org.smdserver.auth.IAuthLocale;
import org.smdserver.auth.IRegisterConfig;
import org.smdserver.auth.RegisterConfig;
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
import org.smdserver.core.small.ISmdLogger;
import org.smdserver.words.IWordsStorage;
import org.smdserver.words.WordsDBStorage;

class USmdCoreFactory implements ISmdCoreFactory
{
	private String localeRBFile;
	private ResourceBundle rb;
	private ResourceBundle serverRB;
	private ISmdLogger logger;
	private ISmdDB db;
	private String basePath;
	
	public USmdCoreFactory (ResourceBundle rb, ResourceBundle serverRB,
			                String localeRBFile,
			                ISmdLogger logger, ISmdDB db, String basePath)
	{
		this.localeRBFile = localeRBFile;
		this.rb = rb;
		this.serverRB = serverRB;
		this.logger = logger;
		this.db = db;
		this.basePath = basePath;
	}
	
	void setDB(ISmdDB db)
	{
		this.db = db;
	}
	
	public IAuthLocale createAuthLocale(Locale locale)
	{
		return new AuthLocale(ResourceBundle.getBundle(localeRBFile, locale));
	}
	
	public IRegisterConfig createRegisterConfig()
	{
		return new RegisterConfig(serverRB);
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
			return new WordsDBStorage(db, logger);
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
			return new UsersDBStorage(db, logger, createCoreConfig().getSecret());
		}
		else
		{
			return null;
		}
	}
	
	public IJSPConfig createJSPConfig()
	{
		return new JSPConfig(rb, basePath, localeRBFile);
	}
	
	public ICoreConfig createCoreConfig()
	{
		return new UCoreConfig(serverRB);
	}

	private IMailConfig createMailConfig()
	{
		return new MailConfig(serverRB);
	}
}
