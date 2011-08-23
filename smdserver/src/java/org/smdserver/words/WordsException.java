package org.smdserver.words;

import org.smdserver.util.SmdException;

public class WordsException extends SmdException
{
	public static final String JSON_ERROR = "JSON error";

	WordsException (String message, Throwable cause, boolean isMessagePublic)
	{
		super(message, cause);
		if(isMessagePublic)
		{
			setPublicMessage(message);
		}
	}
}
