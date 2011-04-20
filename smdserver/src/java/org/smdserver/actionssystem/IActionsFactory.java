package org.smdserver.actionssystem;

import java.util.Map;

interface IActionsFactory
{
	public void registerAction(String name, Class value);
	public void registerMap(Map<String, Class> map);
	public IAction createAction(String name);
}
