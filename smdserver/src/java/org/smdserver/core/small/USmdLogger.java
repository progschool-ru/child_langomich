package org.smdserver.core.small;

abstract class USmdLogger implements ISmdLogger
{
	private static final String EXCEPTION = "Exception: %1$s\n";
	
	private static final String THE_NEXT = "  %3$s.%4$s; %2$d\n";
	
	private static final String CAUSE = "with cause: %1$s\n";
	
	abstract public void log (String message);
	
	public void log (Throwable e)
	{
		log(e, EXCEPTION);

	}
	
	private void log(Throwable e, String template)
	{
		StackTraceElement[] st = e.getStackTrace();
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(template, e.getMessage()));
		
		for(int i = 0; i < st.length; i++)
		{
			sb.append(String.format(THE_NEXT, st[i].getFileName(), st[i].getLineNumber(),
					                    st[i].getClassName(), st[i].getMethodName()));
		}
		
		log(sb.toString());
		
		if(e.getCause() != null)
		{
			log(e.getCause(), CAUSE);
		}
	}
}
