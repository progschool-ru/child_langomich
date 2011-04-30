package org.smdserver;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import org.smdserver.actionssystem.SmdServlet;
import org.smdserver.auth.LoginAction;
import org.smdserver.auth.SetPasswordAction;
import org.smdserver.users.IUsersStorage;
import org.smdserver.users.UsersFileStorage;

public class MainServlet extends SmdServlet
{
	private static final String CONFIG_RESOURCE = "org.smdserver.config";
	private static final String USERS_STORAGE_PATH_KEY = "path.usersStorage";

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

	@Override
	protected IUsersStorage createUsersStorage()
	{
		String path = ResourceBundle.getBundle(CONFIG_RESOURCE).getString(USERS_STORAGE_PATH_KEY);
		return new UsersFileStorage(getServletContext(), path);
	}
}
