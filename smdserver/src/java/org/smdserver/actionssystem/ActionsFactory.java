package org.smdserver.actionssystem;

import java.util.HashMap;
import java.util.Map;

public class ActionsFactory implements IActionsFactory
{
	private Map<String, Class<IAction> > actionClasses = new HashMap<String, Class<IAction> >();

	public void registerAction(String name, Class value)
	{
		if(actionClasses.containsKey(name))
			return;

		actionClasses.put(name, value);
	}

	public IAction createAction(String name)
	{
		try
		{
			if(actionClasses.containsKey(name))
				return actionClasses.get(name).newInstance();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
