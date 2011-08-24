package org.smdserver.core.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.smdserver.actionssystem.IAction;
import org.smdserver.actionssystem.IActionsFactory;
import org.smdserver.core.ISmdServletContext;

public class SmdActionsFactory implements IActionsFactory
{
	private Map<String, Class<ISmdAction> > actionClasses = new HashMap<String, Class<ISmdAction> >();
	private ISmdServletContext servletContext;

	public SmdActionsFactory (ISmdServletContext context)
	{
		servletContext = context;
	}

	public void registerAction (String name, Class value)
	{
		if(actionClasses.containsKey(name))
			return;

		actionClasses.put(name, value);
	}

	public void registerMap (Map<String, Class> map)
	{
		Set<Map.Entry<String, Class> > set = map.entrySet();
		for(Map.Entry<String, Class> entry : set)
		{
			registerAction(entry.getKey(), entry.getValue());
		}
	}

	public IAction createAction (String name)
	{
		try
		{
			if(actionClasses.containsKey(name))
			{
				ISmdAction action = actionClasses.get(name).newInstance();
				action.initServletContext(servletContext);
				return action;
			}
		}
		catch(Exception e)
		{
			servletContext.log(e);
		}
		
		return null;
	}
}
