package org.smdserver.core;

import javax.servlet.ServletContext;

public class CoreInstance 
{
	private static SmdCore instance = new SmdCore();
	
	public static ISmdCore getInstance(ServletContext context)
	{
		instance.setContext(context);
		return instance;
	}
}
