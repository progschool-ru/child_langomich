package org.omich.tool.bcops;

public class BcTaskException extends ParcException
{
	private static final long serialVersionUID = 1L;

	public static final String ERRTYPE_DATABASE    = "BcTaskException.database";
	public static final String ERRTYPE_NETWORK     = "BcTaskException.network";
	public static final String ERRTYPE_NO_INTERNET = "BcTaskException.noInternet";
	public static final String ERRTYPE_OTHER       = "BcTaskException.other";

	public BcTaskException (String type)
	{
		super(type, type);
	}
	
	public BcTaskException (String type, String message, Throwable cause)
	{
		super(type, message, cause);
	}
}
