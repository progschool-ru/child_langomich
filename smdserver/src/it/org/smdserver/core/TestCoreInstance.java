package org.smdserver.core;

import org.smdserver.db.DbException;

public class TestCoreInstance 
{
	private static UTestCore instance;
	
	public static ITestCore getInstance() throws DbException
	{
		if(instance == null)
		{
			instance = new UTestCore();
		}
		return instance;
	}	
}
