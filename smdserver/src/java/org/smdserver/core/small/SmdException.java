package org.smdserver.core.small;

public class SmdException extends Exception
{
	public static final String ERROR = "Error";
	
	private String publicMessage;
	
	protected SmdException (String message)
	{
		super(message);
	}
	
	protected SmdException (String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public String getPublicMessage()
	{
		return publicMessage;
	}
	
	protected void setPublicMessage(String value)
	{
		publicMessage = value;
	}
}
