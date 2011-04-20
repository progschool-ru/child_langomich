package org.smdserver.actionssystem;

import org.smdserver.actionssystem.IAction;

public interface IActionsFactory
{
	public void registerAction(String name, Class value);
	public IAction createAction(String name);
}
