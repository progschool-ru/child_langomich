package org.smdserver.core;

import org.smdserver.actionssystem.IAction;

public interface ISmdAction extends IAction
{
	public void initServletContext (ISmdServletContext context);
}
