package org.omich.tool.log;

public class StackTraceLoggable implements ILoggable
{
	private StackTraceElement[] mTrace;
	
	public StackTraceLoggable (StackTraceElement[] trace)
	{
		mTrace = trace;
	}

	@Override
	public String getShortLogMessage()
	{
		return LogUtil.getShortTraceMessage(0, 100);
	}

	@Override
	public String getFullLogMessage()
	{
		return LogUtil.getStackTrace(mTrace);
	}
}
