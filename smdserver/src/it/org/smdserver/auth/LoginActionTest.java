package org.smdserver.auth;

import org.smdserver.core.WebParams;
import org.smdserver.core.WebActions;
import org.smdserver.core.UsersTestBase;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoginActionTest extends UsersTestBase
{
	private WebConversation wc;
	private WebRequest req;

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		UsersTestBase.setUpClass();
	}

	@AfterClass
	public static void tearDownClass() throws Exception 
	{
		UsersTestBase.tearDownClass();
	}

	@Before
	public void setUp()
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
		assertTrue(json.getBoolean(WebParams.SUCCESS));
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