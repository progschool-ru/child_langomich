package org.smdserver.actionssystem;

import org.smdserver.util.SmdException;

public class ActionException extends SmdException
{
	public static final String MUST_NOT_BE_EMPTY   = "Param %1$s must not be empty";
	public static final String MUST_BE_VALID_EMAIL = "Param %1$s must be valid email, but was %2$s";
	
	protected ActionException (String message)
	{
		super(message);
	}
}
