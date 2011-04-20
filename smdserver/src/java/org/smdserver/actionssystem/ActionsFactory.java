package org.smdserver.actionssystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class ActionsFactory implements IActionsFactory
{
	private Map<String, Class<IAction> > actionClasses = new HashMap<String, Class<IAction> >();
	private ISmdServletContext servletContext;

	public ActionsFactory(ISmdServletContext context)
	{
		servletContext = context;
	}

	public void registerAction(String name, Class value)
	{
		if(actionClasses.containsKey(name))
			return;

		actionClasses.put(name, value);
	}

	public void registerMap(Map<String, Class> map)
	{
		Set<Map.Entry<String, Class> > set = map.entrySet();
		for(Map.Entry<String, Class> entry : set)
		{
			registerAction(entry.getKey(), entry.getValue());
		}
	}

	public IAction createAction(String name)
	{
		try
		{
			if(actionClasses.containsKey(name))
			{
				IAction action = actionClasses.get(name).newInstance();
				action.initServletContext(servletContext);
				return action;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
