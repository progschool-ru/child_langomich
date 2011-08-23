package org.smdserver.util;

import java.io.PrintStream;
import javax.servlet.ServletContext;

public class ComplexSmdLogger extends SmdLogger
{
	private ServletContext servletContext;
	private PrintStream stream;

	public ComplexSmdLogger (ServletContext servletContext, PrintStream stream)
	{
		this.servletContext = servletContext;
		this.stream = stream;
	}
	public void log (String message)
	{
		if(servletContext != null)
		{
			servletContext.log(message);
		}

		if(stream != null)
		{
			stream.println(message);
		}
	}
}
