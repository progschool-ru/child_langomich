package org.smdserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.smdserver.core.NullAction;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import org.smdserver.actionssystem.IActionsFactory;
import org.smdserver.core.ISmdServletContext;
import org.smdserver.actionssystem.SmdServlet;
import org.smdserver.core.SmdServletContext;
import org.smdserver.auth.LoginAction;
import org.smdserver.auth.MobileLoginAction;
import org.smdserver.auth.LogoutAction;
import org.smdserver.auth.SetPasswordAction;
import org.smdserver.auth.RegistrAction;
import org.smdserver.core.SmdActionsFactory;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.SmdDB;
import org.smdserver.jsp.SmdUrl;
import org.smdserver.users.IUsersStorage;
import org.smdserver.users.UsersDBStorage;
import org.smdserver.users.UsersFileStorage;
import org.smdserver.words.GetWordsAction;
import org.smdserver.words.WordsFileStorage;
import org.smdserver.words.AddWordsAction;

public class MainServlet extends SmdServlet
{
	private static final String CONFIG_PARAM = "config";
	private static final String USERS_STORAGE_PATH_KEY = "path.users.storage";
	private static final String WORDS_STORAGE_PATH_KEY = "path.words.storageDir";
	private static final String DEFAULT_ACTION = "default";

	private String configResource;

	protected String getDefaultActionName ()
	{
		return DEFAULT_ACTION;
	}

	protected Map<String, Class> getActionsClasses ()
	{
		Map<String, Class> map = new HashMap<String, Class>();

		map.put(DEFAULT_ACTION, NullAction.class);
		map.put("setPassword", SetPasswordAction.class);
		map.put("login", LoginAction.class);
		map.put("mobileLogin", MobileLoginAction.class);
		map.put("logout", LogoutAction.class);
		map.put("getWords", GetWordsAction.class);
		map.put("addWords", AddWordsAction.class);
		map.put("registr", RegistrAction.class);

		return map;
	}

	protected IActionsFactory createActionsFactory ()
	{
		configResource = getServletContext().getInitParameter(CONFIG_PARAM);
		SmdUrl.initRB(ResourceBundle.getBundle(configResource));
		ResourceBundle rb = ResourceBundle.getBundle(configResource);

		String wordsPath = rb.getString(WORDS_STORAGE_PATH_KEY);

		String usersPath = rb.getString(USERS_STORAGE_PATH_KEY);
		IUsersStorage usersStorage =  new UsersFileStorage(getServletContext().getRealPath(usersPath));
//		IUsersStorage usersStorage = createUsersStorage(rb);

		WordsFileStorage wordsStorage = new WordsFileStorage(getServletContext().getRealPath(wordsPath));
		ISmdServletContext context = new SmdServletContext(usersStorage, wordsStorage, getServletContext());

		wordsStorage.setLogger(context);

		return new SmdActionsFactory(context);
	}

	private IUsersStorage createUsersStorage(ResourceBundle res)
	{
		String serverConfig = res.getString("server.properties.file");
		ResourceBundle rb = ResourceBundle.getBundle(serverConfig);

		String url = rb.getString("db.url");
		String user = rb.getString("db.user");
		String password = rb.getString("db.password");
		try
		{
			Connection connection = DriverManager.getConnection(url, user, password);
			ISmdDB db = new SmdDB(connection);
			return new UsersDBStorage(db);
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
			return null;
		}
	}
}
