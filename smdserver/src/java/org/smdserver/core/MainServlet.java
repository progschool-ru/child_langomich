package org.smdserver.core;

import org.smdserver.core.actions.NullAction;
import java.util.HashMap;
import java.util.Map;
import org.smdserver.actionssystem.IActionsFactory;
import org.smdserver.actionssystem.SmdServlet;
import org.smdserver.auth.LoginAction;
import org.smdserver.auth.MobileLoginAction;
import org.smdserver.auth.LogoutAction;
import org.smdserver.auth.SetPasswordAction;
import org.smdserver.auth.RegistrAction;
import org.smdserver.core.actions.SmdActionsFactory;
import org.smdserver.maintenance.CreateDBAction;
import org.smdserver.maintenance.DeployDBTablesAction;
import org.smdserver.maintenance.DropDBAction;
import org.smdserver.maintenance.DropDBTablesAction;
import org.smdserver.words.GetWordsAction;
import org.smdserver.words.AddWordsAction;
import org.smdserver.words.DeleteWordsAction;

public class MainServlet extends SmdServlet
{
	private static final String DEFAULT_ACTION = "default";

	protected String getDefaultActionName ()
	{
		return DEFAULT_ACTION;
	}

	protected Map<String, Class> getActionsClasses ()
	{
		Map<String, Class> map = new HashMap<String, Class>();

		map.put(DEFAULT_ACTION, NullAction.class);

		map.put("addWords", AddWordsAction.class);
		map.put("getWords", GetWordsAction.class);
		map.put("deleteWords", DeleteWordsAction.class);
		map.put("login", LoginAction.class);
		map.put("logout", LogoutAction.class);
		map.put("mobileLogin", MobileLoginAction.class);
		map.put("registr", RegistrAction.class);
		map.put("setPassword", SetPasswordAction.class);
		
		map.put("maintenanceCreateDB", CreateDBAction.class);
		map.put("maintenanceDeployDBTables", DeployDBTablesAction.class);
		map.put("maintenanceDropDB", DropDBAction.class);
		map.put("maintenanceDropDBTables", DropDBTablesAction.class);

		return map;
	}

	protected IActionsFactory createActionsFactory ()
	{
		ISmdCore core = CoreInstance.getInstance(getServletContext());	
	
		ISmdServletContext context = core.createContext();

		return new SmdActionsFactory(context);
	}
}
