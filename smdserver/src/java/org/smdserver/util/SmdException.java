package org.smdserver.util;

public class SmdException extends Exception
{
	private Exception reason;
	
	protected SmdException (String message)
	{
		super(message);
	}
	
	protected void setReason(Exception e)
	{
		reason = e;
	}

	public Exception getReason()
	{
		return reason;
	}
}
