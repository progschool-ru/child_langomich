package org.smdserver.core.small;

import java.util.UUID;

public class StringUtil 
{	
	public static String escapeHTML(String dirtyValue)
	{
		if(dirtyValue == null)
			return null;
		return dirtyValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;").trim();
	}
	
	public static String generateStringId()
	{
		return UUID.randomUUID().toString();
	}
}
