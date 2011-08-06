package org.smdserver.core;

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
		String serverConfig = resource.getString("server.properties.file");
		ResourceBundle rb = ResourceBundle.getBundle(serverConfig);
		String prefix = rb.getString("db.tablesPrefix");

		if(db == null)
		{
		//TODO: (2.medium) use test properties here:
			db = new SmdDB(rb, new ConsoleSmdLogger(System.out));
		}
		return prefix;
	}
}
