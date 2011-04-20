package org.smdserver;

import java.util.HashMap;
import java.util.Map;
import org.smdserver.actionssystem.SmdServlet;
import org.smdserver.auth.LoginAction;
import org.smdserver.auth.SetPasswordAction;

public class MainServlet extends SmdServlet
{
	protected Class getDefaultActionClass()
	{
		return FirstAction.class;
	}

	protected Map<String, Class> getActionsClasses()
	{
		Map<String, Class> map = new HashMap<String, Class>();
		
		map.put("setPassword", SetPasswordAction.class);
		map.put("login", LoginAction.class);

		return map;
	}
}
