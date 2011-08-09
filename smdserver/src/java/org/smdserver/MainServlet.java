package org.smdserver;

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
import org.smdserver.core.ComplexSmdLogger;
import org.smdserver.core.ISmdLogger;
import org.smdserver.core.SmdActionsFactory;
import org.smdserver.db.DbException;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.SmdDB;
import org.smdserver.jsp.SmdUrl;
import org.smdserver.maintenance.CreateDBAction;
import org.smdserver.maintenance.DeployDBTablesAction;
import org.smdserver.maintenance.DropDBAction;
import org.smdserver.maintenance.DropDBTablesAction;
import org.smdserver.users.IUsersStorage;
import org.smdserver.users.UsersDBStorage;
import org.smdserver.users.UsersFileStorage;
import org.smdserver.words.GetWordsAction;
import org.smdserver.words.WordsFileStorage;
import org.smdserver.words.AddWordsAction;
import org.smdserver.words.IWordsStorage;
import org.smdserver.words.WordsDBStorage;

public class MainServlet extends SmdServlet
{
	private static final String CONFIG_PARAM = "config";
	private static final String USERS_STORAGE_PATH_KEY = "path.users.storage";
	private static final String WORDS_STORAGE_PATH_KEY = "path.words.storageDir";
	private static final String SERVER_PROPERTIES_FILE_KEY = "server.properties.file";
	private static final String DEFAULT_ACTION = "default";

	private String configResource;
	private ISmdDB db;

	@Override
	public void destroy()
	{
		if(db != null)
		{
			db.close();
		}
	}

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
		configResource = getServletContext().getInitParameter(CONFIG_PARAM);
		SmdUrl.initRB(ResourceBundle.getBundle(configResource));
		ResourceBundle rb = ResourceBundle.getBundle(configResource);	

		ISmdLogger logger = new ComplexSmdLogger(getServletContext(), System.out);

//		IWordsStorage wordsStorage = createWordsFileStorage(rb, logger);
		IWordsStorage wordsStorage = createWordsStorage(rb, logger);

//		IUsersStorage usersStorage =  createUsersFileStorage(rb, logger);
		IUsersStorage usersStorage = createUsersStorage(rb, logger);
		
		ISmdServletContext context = new SmdServletContext(usersStorage, 
				                                           wordsStorage, 
				                                           configResource,
				                                           logger);

		return new SmdActionsFactory(context);
	}

	private IWordsStorage createWordsFileStorage(ResourceBundle res, ISmdLogger logger)
	{
		String wordsPath = res.getString(WORDS_STORAGE_PATH_KEY);
		return new WordsFileStorage(getServletContext().getRealPath(wordsPath), logger);
	}

	private IWordsStorage createWordsStorage(ResourceBundle res, ISmdLogger logger)
	{
		String prefix = initDBAndGetPrefix(res, logger);

		if(db != null)
		{
			return new WordsDBStorage(db, prefix);
		}
		else
		{
			return null;
		}
	}

	private IUsersStorage createUsersFileStorage(ResourceBundle res, ISmdLogger logger)
	{
		String usersPath = res.getString(USERS_STORAGE_PATH_KEY);
		return  new UsersFileStorage(getServletContext().getRealPath(usersPath));
	}

	private IUsersStorage createUsersStorage(ResourceBundle res, ISmdLogger logger)
	{
		String prefix = initDBAndGetPrefix(res, logger);

		if(db != null)
		{
			return new UsersDBStorage(db, prefix);
		}
		else
		{
			return null;
		}
	}

	private String initDBAndGetPrefix(ResourceBundle res, ISmdLogger logger)
	{
		String serverConfig = res.getString(SERVER_PROPERTIES_FILE_KEY);
		ResourceBundle rb = ResourceBundle.getBundle(serverConfig);
		String prefix = rb.getString("db.tablesPrefix");

		if(db == null)
		{	
			try
			{
				db = new SmdDB(rb, logger);
			}
			catch (DbException e)
			{
				logger.log(e.getMessage());
			}
		}

		return prefix;
	}
}
