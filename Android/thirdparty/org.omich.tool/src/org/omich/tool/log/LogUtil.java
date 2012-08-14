package org.omich.tool.log;

import org.omich.tool.constants.Strings;

public class LogUtil 
{
	public static final String NULL_STRING_E = "E__NULL__"; //$NON-NLS-1$
	public static final String NULL_STRING_S = "S__NULL__"; //$NON-NLS-1$
	public static final String NULL_STRING_C = "C__NULL__"; //$NON-NLS-1$
	
	public static String getShortLogMessage (Throwable er)
	{
		if(er == null)
			return NULL_STRING_E;
		
		return er.getClass().getSimpleName() + ": " + getNotNullMessage(er.getMessage()); //$NON-NLS-1$
	}
	
	public static String getFullLogMessage (Throwable er)
	{
		if(er == null)
			return NULL_STRING_E;
		
		return er.getClass().getSimpleName() + ": "  //$NON-NLS-1$
				+ getNotNullMessage(er.getMessage()) + Strings.CH_ENTER
				+ getStackTrace(er.getStackTrace());
	}

	public static String getNotNullMessage (String msg)
	{
		return msg == null ? NULL_STRING_S : msg;
	}
	
	public static String getStackTrace (StackTraceElement[] ste)
	{
		return getLongTraceMessage(ste, "\n\t"); //$NON-NLS-1$
	}

	public static String getLongTraceMessage (StackTraceElement[] ste, String sep)
	{
		if(ste == null)
			return Strings.EMPTY;

		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 10 && i < ste.length; ++i)
		{
			sb.append(ste[i].toString());
			sb.append(sep);
		}
		return sb.toString();
	}

	public static String getShortTraceMessage (int start, int end)
	{
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		StringBuilder sb = new StringBuilder();
		for(int i = start; i <= end && i < trace.length; i++)
		{
			sb.append(getTraceMessageItem(trace[i])).append(' ');
		}
		return sb.toString();
	}

	private static String getTraceMessageItem (StackTraceElement elem)
	{
		String fileName = elem.getFileName();
		int ind = fileName.indexOf('.');
		if(ind >= 0)
		{
			fileName = fileName.substring(0, ind);
		}
		return fileName + Strings.COLON + elem.getLineNumber();
	}
}
