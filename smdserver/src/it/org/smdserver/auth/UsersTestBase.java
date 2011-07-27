package org.smdserver.auth;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import org.smdserver.users.UsersFileStorage;
import org.xml.sax.SAXException;

public class UsersTestBase
{
	public static final String USER_ID = "TestUUID";
	public static final String LOGIN = "testLogin";
	public static final String PASSWORD = "testPassword";
	public static final String COOKIE_HEADER = "SET-COOKIE";

	private static ResourceBundle resource;
	private static UsersFileStorage storage;

	protected static void setUpClass() throws Exception
	{
		resource = ResourceBundle.getBundle("org.smdserver.config");

		File file = new File(resource.getString("test.server.path") + resource.getString("path.users.storage"));
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

	protected static JSONObject getJSONResource(WebConversation wc, WebRequest req) throws JSONException, IOException, SAXException
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

		String text = resp.getText();
		return new JSONObject(text);
	}
}
