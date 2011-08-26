package org.smdserver.core;

import org.smdserver.db.DbException;
import org.smdserver.users.IUsersStorage;

public class IntegrationTestBase 
{
	public static  IUsersStorage usersStorage;
	public static ITestConfig testConfig;
	
	protected static ITestCore getCore() throws DbException
	{
		return TestCoreInstance.getInstance();
	}
	
	protected static IUsersStorage getUsersStorage() throws DbException
	{
		if(usersStorage == null)
		{
			usersStorage = getCore().getFactory().createUsersStorage();
		}
		return usersStorage;
	}
	
	protected static ITestConfig getTestConfig() throws DbException
	{
		if(testConfig == null)
		{
			testConfig = getCore().getFactory().createTestConfig();
		}
		return testConfig;
	}
}
