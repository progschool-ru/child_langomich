package org.smdserver.core;

public class ConsoleSmdLogger implements ISmdLogger
{
	public void log (String message)
	{
		System.out.println(message);
	}
}
