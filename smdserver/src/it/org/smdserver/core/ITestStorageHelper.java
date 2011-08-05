package org.smdserver.core;

import java.io.IOException;
import java.util.ResourceBundle;
import org.smdserver.words.IWordsStorage;

public interface ITestStorageHelper
{
	public void openUsersStorage(ResourceBundle resource,
							String userId, String login, String password) throws Exception;
	public void closeUsersStorage();

	public IWordsStorage openWordsStorage(ResourceBundle resource, String userId) throws IOException;
	public void closeWordsStorage(ResourceBundle resource, String userId);
}
