package org.smdserver.actionssystem;

public abstract class Action implements IAction
{
	private ISmdServletContext servletContext;
	
	public void initServletContext(ISmdServletContext context)
	{
		if(servletContext == null)
			servletContext = context;
	}

	protected ISmdServletContext getServletContext()
	{
		return servletContext;
	}
}
