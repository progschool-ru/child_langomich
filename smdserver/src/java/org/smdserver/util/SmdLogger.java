package org.smdserver.util;

abstract class SmdLogger implements ISmdLogger
{
	private static final String EXCEPTION = "Exception: %1$s\n"
			+ "Top of the stack:\n"
			+ "  file -   %2$s\t"
			+ "  line -   %3$d\n"
			+ "  class -  %4$s\t"
			+ "  method - %5$s\n";
	
	private static final String CAUSE = "with cause: %1$s\n"
			+ "Top of the stack:\n"
			+ "  file -   %2$s\t"
			+ "  line -   %3$d\n"
			+ "  class -  %4$s\t"
			+ "  method - %5$s\n";
	
	abstract public void log (String message);
	
	public void log (Throwable e)
	{
		log(e, EXCEPTION);

	}
	
	private void log(Throwable e, String template)
	{
		StackTraceElement ste = e.getStackTrace()[0];
		log(String.format(template, e.getMessage(), 
				ste.getFileName(), ste.getLineNumber(),
				ste.getClassName(), ste.getMethodName()));
		
		if(e.getCause() != null)
		{
			log(e.getCause(), CAUSE);
		}
	}
}
