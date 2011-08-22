package org.smdserver.core;

import org.smdserver.util.ConsoleSmdLogger;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.smdserver.db.DBConfig;
import org.smdserver.db.DbException;
import org.smdserver.db.IDBConfig;
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
		initDBAndGetPrefix(resource);
		usersStorage = new UsersDBStorage(db);
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
		initDBAndGetPrefix(resource);
		wordsStorage = new WordsDBStorage(db);
		return wordsStorage;
	}
	public void closeWordsStorage(ResourceBundle resource, String userId)
	{
		wordsStorage.setUserWords(userId, new ArrayList<Language>());
	}

	private String initDBAndGetPrefix(ResourceBundle resource) throws DbException
	{
		IDBConfig config = new DBConfig("org.smdserver.config", 
				                                        "server.properties.file");
		String prefix = config.getTablesPrefix();

		if(db == null)
		{
		//TODO: (2.medium)[#26068] use test properties here:	
			db = new SmdDB(config, new ConsoleSmdLogger(System.out));
		}
		return prefix;
	}
}
