package org.smdserver.actionssystem;

import java.util.HashMap;
import java.util.Map;

public class ActionsFactory implements IActionsFactory
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
