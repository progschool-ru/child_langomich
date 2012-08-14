package org.omich.tool.bcops;

public class BcTaskException extends ParcException
{
	private static final long serialVersionUID = 1L;

	public static final String ERRTYPE_DATABASE    = "BcTaskException.database"; //$NON-NLS-1$
	public static final String ERRTYPE_NETWORK     = "BcTaskException.network"; //$NON-NLS-1$
	public static final String ERRTYPE_NO_INTERNET = "BcTaskException.noInternet"; //$NON-NLS-1$
	public static final String ERRTYPE_OTHER       = "BcTaskException.other"; //$NON-NLS-1$

	public BcTaskException (String type)
	{
		super(type, type);
	}
	
	public BcTaskException (String type, String message, Throwable cause)
	{
		super(type, message, cause);
	}
}
