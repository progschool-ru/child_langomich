package org.smdserver.auth;

import java.util.HashMap;
import java.util.Map;

public enum ConfirmationType
{
	ADMIN ("admin", new UNoneConfirmationActivity()),
	MAIL  ("mail",  new UNoneConfirmationActivity()),
	NONE  ("none",  new UNoneConfirmationActivity());
	
	private static Map<String, ConfirmationType> types;
	
	private String name;
	private UIConfirmationActivity activity;

	private ConfirmationType(String name, UIConfirmationActivity activity)
	{
		this.name = name;
		this.activity = activity;
		registerInstance();
	}
	
	UIConfirmationActivity getActivity()
	{
		return this.activity;
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
