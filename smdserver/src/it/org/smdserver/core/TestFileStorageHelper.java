package org.smdserver.core;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import org.smdserver.users.UsersFileStorage;
import org.smdserver.words.IWordsStorage;
import org.smdserver.words.WordsFileStorage;

public class TestFileStorageHelper implements ITestStorageHelper
{
	private UsersFileStorage usersStorage;
	private String login;

	public void openUsersStorage(ResourceBundle resource,
							String userId, String login, String password) throws Exception
	{
		File file = new File(resource.getString("test.server.path") + resource.getString("path.users.storage"));
		usersStorage = new UsersFileStorage(file.getAbsolutePath());
		usersStorage.createUser(userId, login, password);
		this.login = login;
	}

	public void closeUsersStorage()
	{
		usersStorage.removeUserByLogin(login);
	}

	public IWordsStorage openWordsStorage(ResourceBundle resource, String userId) throws IOException
	{
		File file = new File(resource.getString("test.server.path") +
				             resource.getString("path.words.storageDir") +
							  userId + ".dat");

		file.createNewFile();
		file.setWritable(true, false);
		
		return new WordsFileStorage(file.getParentFile().getAbsolutePath(), new ConsoleSmdLogger(System.out));
	}

	public void closeWordsStorage(ResourceBundle resource, String userId)
	{
		File file = new File(resource.getString("test.server.path") +
				             resource.getString("path.words.storageDir") +
							 userId + ".dat");
		file.delete();
	}
}
