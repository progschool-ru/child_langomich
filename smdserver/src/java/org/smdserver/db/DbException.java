package org.smdserver.db;

import org.smdserver.core.SmdException;

public class DbException extends SmdException
{
	public static final String CANT_CONNECT_TO_DATABASE = "Can't connect to database";
	public static final String ROLLBACK_ERROR = "Rollback error";

	DbException (String message)
	{
		super(message);
	}
	
	DbException (String message, Exception reason)
	{
		super(message);
		super.setReason(reason);
	}
}
