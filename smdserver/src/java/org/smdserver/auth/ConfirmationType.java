package org.smdserver.auth;

import java.util.HashMap;
import java.util.Map;

public enum ConfirmationType
{
	ADMIN ("admin"),
	MAIL ("mail"),
	NONE ("none");
	
	private static Map<String, ConfirmationType> types;
	
	private String name;

	private ConfirmationType(String name)
	{
		this.name = name;
		registerInstance();
	}
	
	public static ConfirmationType getType (String name)
	{
		return types.get(name);
	}
	
	private void registerInstance()
	{
		if(types == null)
		{
			 types = new HashMap<String, ConfirmationType>();
		}
		types.put(name, this);
	}
}
