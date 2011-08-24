package org.smdserver.core;

import javax.servlet.ServletContext;

public class CoreInstance 
{
	private static USmdCore instance = new USmdCore();
	
	public static ISmdCore getInstance(ServletContext context)
	{
		instance.setContext(context);
		return instance;
	}
}
