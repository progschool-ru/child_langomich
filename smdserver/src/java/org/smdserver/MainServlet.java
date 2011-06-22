package org.smdserver;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import org.smdserver.actionssystem.IActionsFactory;
import org.smdserver.core.ISmdServletContext;
import org.smdserver.actionssystem.SmdServlet;
import org.smdserver.core.SmdServletContext;
import org.smdserver.auth.EnterAction;
import org.smdserver.auth.LoginAction;
import org.smdserver.auth.LogoutAction;
import org.smdserver.auth.SetPasswordAction;
import org.smdserver.auth.RegistrAction;
import org.smdserver.core.SmdActionsFactory;
import org.smdserver.users.IUsersStorage;
import org.smdserver.users.UsersFileStorage;
import org.smdserver.words.GetWordsAction;
import org.smdserver.words.IWordsStorage;
import org.smdserver.words.SetWordsAction;
import org.smdserver.words.WordsFileStorage;
import org.smdserver.words.AddWordsAction;

public class MainServlet extends SmdServlet
{
	private static final String CONFIG_RESOURCE = "org.smdserver.config";
	private static final String USERS_STORAGE_PATH_KEY = "path.users.storage";
	private static final String WORDS_STORAGE_PATH_KEY = "path.words.storageDir";

	protected Class getDefaultActionClass ()
	{
		return FirstAction.class;
	}

	protected Map<String, Class> getActionsClasses ()
	{
		Map<String, Class> map = new HashMap<String, Class>();

                map.put("enter", EnterAction.class);
                map.put("setPassword", SetPasswordAction.class);
		map.put("login", LoginAction.class);
                map.put("logout", LogoutAction.class);
		map.put("setWords", SetWordsAction.class);
		map.put("getWords", GetWordsAction.class);
                map.put("addWord", AddWordsAction.class);
                map.put("registr", RegistrAction.class);

		return map;
	}

	protected IActionsFactory createActionsFactory ()
	{
		String usersPath = ResourceBundle.getBundle(CONFIG_RESOURCE).getString(USERS_STORAGE_PATH_KEY);
                String wordsPath = ResourceBundle.getBundle(CONFIG_RESOURCE).getString(WORDS_STORAGE_PATH_KEY);
		IUsersStorage usersStorage =  new UsersFileStorage(getServletContext().getRealPath(usersPath));
		IWordsStorage wordsStorage = new WordsFileStorage(getServletContext().getRealPath(wordsPath));
		ISmdServletContext context = new SmdServletContext(usersStorage, wordsStorage, getServletContext());

		return new SmdActionsFactory(context);
	}
}
