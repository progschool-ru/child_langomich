package org.smdserver.db;

import org.smdserver.core.small.SmdException;

public class DbException extends SmdException
{
	public static final String CANT_CONNECT_TO_DATABASE = "Can't connect to database";
	public static final String ROLLBACK_ERROR = "Rollback error";
	public static final String PUBLIC_MESSAGE = "DB Error";

	DbException (String message)
	{
		super(message);
		setPublicMessage(PUBLIC_MESSAGE);
	}

	public DbException(String message, Throwable cause, String publicMessage) 
	{
		super(message, cause);
		setPublicMessage(publicMessage);
	}
	
	DbException (String message, Throwable cause)
	{
		super(message, cause);
	}
}
