package org.smdserver.core;

import java.io.File;
import java.util.ResourceBundle;
import org.smdserver.users.UsersFileStorage;

class TestFileStorageHelper implements ITestStorageHelper
{
	private UsersFileStorage usersStorage;
	private String login;

	public void openUsersStorage(ResourceBundle resource,
							String userId, String login, String password) throws Exception
	{
		File file = new File(resource.getString("test.server.path") + resource.getString("path.users.storage"));
		usersStorage = new UsersFileStorage(file.getAbsolutePath());
		boolean success = usersStorage.createUser(userId, login, password);
		this.login = login;
	}

	public void closeUsersStorage()
	{
		usersStorage.removeUserByLogin(login);
	}

	public void openWordsStorage(ResourceBundle resource, String userId)
	{
		File file = new File(resource.getString("test.server.path") +
				             resource.getString("path.words.storageDir") +
							 userId + ".dat");
	}

	public void closeWordsStorage(ResourceBundle resource, String userId)
	{
		File file = new File(resource.getString("test.server.path") +
				             resource.getString("path.words.storageDir") +
							 userId + ".dat");
		file.delete();
	}
}
