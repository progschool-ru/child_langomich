package org.smdserver.core;

import com.ccg.util.JavaString;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import org.smdserver.db.DbException;
import org.xml.sax.SAXException;

public class UsersTestBase extends IntegrationTestBase
{
	public static final String USER_ID = "TestUUID";
	public static final String LOGIN = "testLogin";
	public static final String PASSWORD = "testPassword";
	public static final String COOKIE_HEADER = "SET-COOKIE";

	protected static WebRequest createActionRequest(String actionName) throws DbException
	{
		String url = getTestConfig().getTestUrl() +
						getTestConfig().getTestUrlAction() + actionName;
		return new GetMethodWebRequest(url);
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
	
	protected static void fillUsers() throws DbException
	{
		getUsersStorage().createUser(USER_ID, LOGIN, PASSWORD, "some@example.org", "Peter Ivanov");
	}
	
	protected static void clearUsers() throws DbException
	{
		getUsersStorage().removeUserById(USER_ID);
	}
}
