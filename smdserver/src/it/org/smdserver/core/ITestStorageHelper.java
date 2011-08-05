package org.smdserver.core;

import java.util.ResourceBundle;

public interface ITestStorageHelper
{
	public void openUsersStorage(ResourceBundle resource,
							String userId, String login, String password) throws Exception;
	public void closeUsersStorage();

	public void openWordsStorage(ResourceBundle resource, String userId);
	public void closeWordsStorage(ResourceBundle resource, String userId);
}
