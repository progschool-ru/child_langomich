package org.smdserver.auth;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import org.json.JSONException;
import org.json.JSONObject;
import org.smdserver.users.UsersFileStorage;

public class UsersTestBase
{
	public static final String USER_ID = "TestUUID";
	public static final String LOGIN = "testLogin";
	public static final String PASSWORD = "testPassword";

	private static ResourceBundle resource;
	private static UsersFileStorage storage;

	protected static void setUpClass() throws Exception
	{
		resource = ResourceBundle.getBundle("org.smdserver.config");

		File file = new File(resource.getString("test.server.path") + resource.getString("path.users.storage"));
		System.out.println(file.getAbsolutePath());
		storage = new UsersFileStorage(file.getAbsolutePath());
		storage.createUser(USER_ID, LOGIN, PASSWORD);
	}

	protected static void tearDownClass() throws Exception
	{
		storage.removeUserByLogin(LOGIN);
	}

	protected static String getActionUrl()
	{
		return getResource().getString("test.url") + getResource().getString("test.url.action");
	}

	private static ResourceBundle getResource()
	{
		return resource;
	}

	protected static JSONObject getJSONResource(WebConversation wc, WebRequest req) throws JSONException, IOException
	{
		String text = wc.getResource(req).getText();
//		System.out.println(text);
		return new JSONObject(text);
	}
}
