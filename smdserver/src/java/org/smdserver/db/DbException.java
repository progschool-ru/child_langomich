package org.smdserver.db;

import org.smdserver.core.SmdException;

public class DbException extends SmdException
{
	public static final String CANT_CONNECT_TO_DATABASE = "Can't connect to database";

	DbException (String message)
	{
		super(message);
	}
}
