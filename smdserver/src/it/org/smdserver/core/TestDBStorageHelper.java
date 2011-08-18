package org.smdserver.core;

import org.smdserver.util.ConsoleSmdLogger;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.smdserver.db.DbException;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.SmdDB;
import org.smdserver.users.UsersDBStorage;
import org.smdserver.words.IWordsStorage;
import org.smdserver.words.Language;
import org.smdserver.words.WordsDBStorage;

class TestDBStorageHelper implements ITestStorageHelper
{
	private UsersDBStorage usersStorage;
	private WordsDBStorage wordsStorage;
	private ISmdDB db;
	private String userId;

	public void openUsersStorage(ResourceBundle resource,
							String userId, String login, String password) throws Exception
	{
		String prefix = initDBAndGetPrefix(resource);
		usersStorage = new UsersDBStorage(db, prefix);
		usersStorage.createUser(userId, login, password);
		this.userId = userId;
	}
	
	public void closeUsersStorage()
	{
		usersStorage.removeUserById(userId);
		db.close();
		db = null;
	}

	public IWordsStorage openWordsStorage(ResourceBundle resource, String userId) throws Exception
	{
		String prefix = initDBAndGetPrefix(resource);
		wordsStorage = new WordsDBStorage(db, prefix);
		return wordsStorage;
	}
	public void closeWordsStorage(ResourceBundle resource, String userId)
	{
		wordsStorage.setUserWords(userId, new ArrayList<Language>());
	}

	private String initDBAndGetPrefix(ResourceBundle resource) throws DbException
	{
		IConfigProperties config = new ConfigProperties("org.smdserver.config", 
				                                        "server.properties.file", 
				                                         userId);
		String prefix = config.getTablesPrefix();

		if(db == null)
		{
		//TODO: (2.medium)[#26068] use test properties here:	
			db = new SmdDB(config, new ConsoleSmdLogger(System.out));
		}
		return prefix;
	}
}
