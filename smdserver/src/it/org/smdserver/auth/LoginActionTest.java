package org.smdserver.auth;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.smdserver.users.UsersFileStorage;
import static org.junit.Assert.*;

public class LoginActionTest
{
	private static final String USER_ID = "TestUUID";
	private static final String LOGIN = "testLogin";
	private static final String PASSWORD = "testPassword";

	private static ResourceBundle resource;
	private static UsersFileStorage storage;

	private WebConversation wc;
	private WebRequest req;

    public LoginActionTest() {
    }

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		resource = ResourceBundle.getBundle("org.smdserver.config");

		File file = new File(resource.getString("test.server.path") + resource.getString("path.users.storage"));
		System.out.println(file.getAbsolutePath());
		storage = new UsersFileStorage(file.getAbsolutePath());
		storage.createUser(USER_ID, LOGIN, PASSWORD);
	}

	@AfterClass
	public static void tearDownClass() throws Exception 
	{
		storage.removeUserByLogin(LOGIN);
	}

    @Before
    public void setUp()
	{
		String url = resource.getString("test.url") + resource.getString("test.url.action");
		wc = new WebConversation();
		req = new GetMethodWebRequest(url + "/login");
    }

	@After
	public void tearDown()
	{
		wc = null;
		req = null;
	}

	@Test
	public void testIncorrectPassword() throws IOException, JSONException
	{
		req.setParameter("login", LOGIN);
		req.setParameter("password", "incorrect" + PASSWORD);
		WebResponse resp = wc.getResource(req);

		String text = resp.getText();
		JSONObject json = new JSONObject(text);
		assertFalse(json.getBoolean("success"));
	}

	@Test
	public void testCorrectPassword() throws IOException, JSONException
	{
		req.setParameter("login", LOGIN);
		req.setParameter("password", PASSWORD);
		WebResponse resp = wc.getResource(req);

		String text = resp.getText();
		JSONObject json = new JSONObject(text);
		assertTrue(json.getBoolean("success"));
	}
}