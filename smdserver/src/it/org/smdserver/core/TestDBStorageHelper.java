package org.smdserver.core;

import java.util.ResourceBundle;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.SmdDB;
import org.smdserver.users.UsersDBStorage;

class TestDBStorageHelper implements ITestStorageHelper
{
	private UsersDBStorage usersStorage;
	private ISmdDB db;
	private String userId;

	public void openUsersStorage(ResourceBundle resource,
							String userId, String login, String password) throws Exception
	{
		//TODO: (2.medium) use test properties here:
		String serverConfig = resource.getString("server.properties.file");
		ResourceBundle rb = ResourceBundle.getBundle(serverConfig);

		db = new SmdDB(rb, new ConsoleSmdLogger(System.out));
		usersStorage = new UsersDBStorage(db);
		boolean success = usersStorage.createUser(userId, login, password);
		this.userId = userId;
	}
	
	public void closeUsersStorage()
	{
		boolean success = usersStorage.removeUserById(userId);
		db.close();
	}

	public void openWordsStorage(ResourceBundle resource, String userId)
	{}
	public void closeWordsStorage(ResourceBundle resource, String userId)
	{}
}
