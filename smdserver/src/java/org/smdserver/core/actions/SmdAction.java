package org.smdserver.core.actions;

import org.smdserver.actionssystem.Action;
import org.smdserver.core.ISmdServletContext;
import org.smdserver.core.small.ISmdLogger;

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

	protected ISmdLogger getLogger()
	{
		return servletContext.getLogger();
	}
}
