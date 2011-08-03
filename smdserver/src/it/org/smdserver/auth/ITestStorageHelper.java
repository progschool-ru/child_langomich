package org.smdserver.auth;

import java.util.ResourceBundle;

interface ITestStorageHelper
{
	public void openUsersStorage(ResourceBundle resource,
							String userId, String login, String password) throws Exception;
	public void closeUsersStorage();
}
