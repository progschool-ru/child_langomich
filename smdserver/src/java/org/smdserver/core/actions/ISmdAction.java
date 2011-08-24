package org.smdserver.core.actions;

import org.smdserver.actionssystem.IAction;
import org.smdserver.core.ISmdServletContext;

public interface ISmdAction extends IAction
{
	public void initServletContext (ISmdServletContext context);
}
