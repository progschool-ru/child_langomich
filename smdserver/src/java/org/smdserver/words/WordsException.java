package org.smdserver.words;

import org.smdserver.core.SmdException;

public class WordsException extends SmdException
{
	public static String ICORRECT_DATE_FORMAT = "Incorrect Date format";
	public static String JSON_ERROR = "JSON error";

	WordsException(String message)
	{
		super(message);
	}
}
