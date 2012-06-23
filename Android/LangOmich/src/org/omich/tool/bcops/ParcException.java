package org.omich.tool.bcops;

public class ParcException extends Exception
{
	private static final long serialVersionUID = 1L;

	private String mType;

	public ParcException (String type, String message)
	{
		super(message);
		mType = type;
	}
	
	public ParcException (String type, String message, Throwable cause)
	{
		super(message, cause);
		mType = type;
	}
	
	public String getType ()
	{
		return mType;
	}
	
	public ErrorParcelable createErrorParcelable ()
	{
		ErrorParcelable err = new ErrorParcelable(this);
		err.type = mType;
		return err;
	}
}
