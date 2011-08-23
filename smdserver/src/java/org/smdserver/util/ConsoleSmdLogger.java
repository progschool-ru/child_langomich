package org.smdserver.util;

import java.io.PrintStream;

public class ConsoleSmdLogger extends SmdLogger
{
	private PrintStream stream;

	public ConsoleSmdLogger(PrintStream stream)
	{
		this.stream = stream;
	}
	
	public void log (String message)
	{
		if(stream != null)
		{
			stream.println(message);
		}
	}
}
