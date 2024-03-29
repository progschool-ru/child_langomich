package org.smdserver.auth;

import org.smdserver.core.WebParams;
import org.smdserver.core.WebActions;
import org.smdserver.core.UsersTestBase;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.smdserver.db.DbException;
import static org.junit.Assert.*;

public class LoginActionTest extends UsersTestBase
{
	private WebConversation wc;
	private WebRequest req;

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		fillUsers();
	}

	@AfterClass
	public static void tearDownClass() throws Exception 
	{
		clearUsers();
	}

	@Before
	public void setUp() throws DbException
	{
		wc = new WebConversation();
		req = createActionRequest(WebActions.LOGIN);
	}

	@After
	public void tearDown()
	{
		wc = null;
		req = null;
	}

	@Test
	public void testIncorrectPassword() throws Exception
	{
		req.setParameter(WebParams.LOGIN, LOGIN);
		req.setParameter(WebParams.PASSWORD, "incorrect" + PASSWORD);
		
		JSONObject json = getJSONResource(wc, req);
		assertFalse(json.getBoolean(WebParams.SUCCESS));
	}

	@Test
	public void testCorrectPassword() throws Exception
	{
		req.setParameter(WebParams.LOGIN, LOGIN);
		req.setParameter(WebParams.PASSWORD, PASSWORD);

		JSONObject json = getJSONResource(wc, req);
		JSONObject user = json.getJSONObject("user");
		assertTrue(json.getBoolean(WebParams.SUCCESS));
		assertNotNull(user);
		assertEquals(LOGIN, user.getString("login"));
		assertFalse(user.has("psw"));
		assertFalse(user.has("password"));
	}

	@Test
	public void testPasswordIsNull() throws Exception
	{
		req.setParameter(WebParams.LOGIN, LOGIN);
		JSONObject json = getJSONResource(wc, req);
		assertFalse(json.getBoolean(WebParams.SUCCESS));
	}

	@Test
	public void testAllParamsAreNull() throws Exception
	{
		JSONObject json = getJSONResource(wc, req);
		assertFalse(json.getBoolean(WebParams.SUCCESS));
	}
}