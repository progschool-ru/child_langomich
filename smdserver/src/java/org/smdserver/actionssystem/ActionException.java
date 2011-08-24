package org.smdserver.actionssystem;

import org.smdserver.core.small.SmdException;

public class ActionException extends SmdException
{
	public static final String MUST_NOT_BE_EMPTY   = "Param '%1$s' must not be empty";
	public static final String MUST_BE_VALID_EMAIL = "Param '%1$s' must be valid email, but was '%2$s'";
	public static final String CANT_CREATE_ACTIOIN = "Can't create action %1$s";
	
	protected ActionException (String message, String publicMessage)
	{
		super(message);
		setPublicMessage(publicMessage);
	}
}
