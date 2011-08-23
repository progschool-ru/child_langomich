package org.smdserver.core;

import org.smdserver.actionssystem.Action;

public abstract class SmdAction extends Action implements ISmdAction
{
	private ISmdServletContext servletContext;

	public void initServletContext (ISmdServletContext context)
	{
		if(servletContext == null)
			servletContext = context;
	}

	protected ISmdServletContext getServletContext ()
	{
		return servletContext;
	}

	@Override
	protected void log (String message)
	{
		getServletContext().log(message);
	}
	
	@Override
	protected void log (Throwable e)
	{
		getServletContext().log(e);
	}
}
