package org.smdserver.auth;

import com.ccg.util.JavaString;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.SmdDB;
import org.smdserver.users.UsersDBStorage;
import org.smdserver.users.UsersFileStorage;
import org.xml.sax.SAXException;

public class UsersTestBase
{
	public static final String USER_ID = "TestUUID";
	public static final String LOGIN = "testLogin";
	public static final String PASSWORD = "testPassword";
	public static final String COOKIE_HEADER = "SET-COOKIE";

	private static ResourceBundle resource;
//	private static UsersDBStorage storage;
	private static UsersFileStorage storage;

	protected static void setUpClass() throws Exception
	{
		resource = ResourceBundle.getBundle("org.smdserver.config");

		File file = new File(resource.getString("test.server.path") + resource.getString("path.users.storage"));
		storage = new UsersFileStorage(file.getAbsolutePath());

//		//TODO: (2.medium) use test properties here:
//		String serverConfig = resource.getString("server.properties.file");
//		ResourceBundle rb = ResourceBundle.getBundle(serverConfig);
//
//		String url = rb.getString("db.url");
//		String user = rb.getString("db.user");
//		String password = rb.getString("db.password");
//
//		Connection connection = DriverManager.getConnection(url, user, password);
//		ISmdDB db = new SmdDB(connection);
//		storage = new UsersDBStorage(db);
		boolean success = storage.createUser(USER_ID, LOGIN, PASSWORD);
		System.out.println(success ? "created" : "create failure");
	}

	protected static void tearDownClass() throws Exception
	{
		storage.removeUserByLogin(LOGIN);
//		boolean success = storage.removeUserById(USER_ID);
//		System.out.println(success ? "removed" : "remove failure");
	}

	protected static String getActionUrl()
	{
		return getResource().getString("test.url") + getResource().getString("test.url.action");
	}

	protected static ResourceBundle getResource()
	{
		return resource;
	}

	protected static String getTextResource(WebConversation wc, WebRequest req)
			throws IOException, SAXException
	{
		WebResponse resp = wc.getResponse(req);

		if(wc.getCookieDetails(WebParams.JSESSIONID) == null
				&& resp.getHeaderField(COOKIE_HEADER) != null)
		{
			Pattern regex = Pattern.compile(".*" + WebParams.JSESSIONID + "=(\\w*);.*");
			Matcher matcher = regex.matcher(resp.getHeaderField(COOKIE_HEADER));
			matcher.find();
			String sessionId = matcher.group(1);
			wc.putCookie(WebParams.JSESSIONID, sessionId);
		}
		return resp.getText();
	}

	protected static JSONObject getJSONResource(WebConversation wc, WebRequest req)
			throws IOException, ParseException, JSONException, SAXException
	{
		String text = JavaString.decode(getTextResource(wc, req));
		return new JSONObject(text);
	}
}
