package org.smdserver;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import org.apache.juli.logging.LogFactory;
import org.smdserver.actionssystem.IActionsFactory;
import org.smdserver.core.ISmdServletContext;
import org.smdserver.actionssystem.SmdServlet;
import org.smdserver.core.SmdServletContext;
import org.smdserver.auth.LoginAction;
import org.smdserver.auth.SetPasswordAction;
import org.smdserver.core.SmdActionsFactory;
import org.smdserver.users.IUsersStorage;
import org.smdserver.users.UsersFileStorage;

public class MainServlet extends SmdServlet
{
	private static final String CONFIG_RESOURCE = "org.smdserver.config";
	private static final String USERS_STORAGE_PATH_KEY = "path.users.storage";
//	private static final String WORDS_STORAGE_PATH_KEY = "path.words.storageDir";

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

	protected IActionsFactory createActionsFactory()
	{
		String path = ResourceBundle.getBundle(CONFIG_RESOURCE).getString(USERS_STORAGE_PATH_KEY);
		IUsersStorage usersStorage =  new UsersFileStorage(getServletContext(), path);
		ISmdServletContext context = new SmdServletContext(usersStorage);

		return new SmdActionsFactory(context);
	}
}
