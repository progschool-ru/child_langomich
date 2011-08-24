package org.smdserver.core.small;

import java.io.PrintStream;

public class ConsoleSmdLogger extends USmdLogger
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
