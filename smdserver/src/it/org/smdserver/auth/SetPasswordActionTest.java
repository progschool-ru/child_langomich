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

public class SetPasswordActionTest extends UsersTestBase
{
	private WebConversation wc;

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
	}

	@After
	public void tearDown()
	{
		wc = null;
	}

	@Test
	public void testSuccessChange() throws Exception
	{
		WebRequest req = new GetMethodWebRequest(getActionUrl() + WebActions.LOGIN);
		req.setParameter(WebParams.LOGIN, LOGIN);
		req.setParameter(WebParams.PASSWORD, PASSWORD);
		JSONObject loginResponse = getJSONResource(wc, req);

		String newPassword = "lalala";

		req = new GetMethodWebRequest(getActionUrl() + WebActions.SET_PASSWORD);
		req.setParameter(WebParams.PASSWORD, newPassword);
		JSONObject setPasswordResponse = getJSONResource(wc, req);

		req = new GetMethodWebRequest(getActionUrl() + WebActions.LOGIN);
		req.setParameter(WebParams.LOGIN, LOGIN);
		req.setParameter(WebParams.PASSWORD, PASSWORD);
		JSONObject failureLoginResponse = getJSONResource(wc, req);

		req.setParameter(WebParams.LOGIN, LOGIN);
		req.removeParameter(WebParams.PASSWORD);
		req.setParameter(WebParams.PASSWORD, newPassword);
		JSONObject successLoginResponse = getJSONResource(wc, req);

		assertTrue(loginResponse.getBoolean(WebParams.SUCCESS));
		assertTrue(setPasswordResponse.getBoolean(WebParams.SUCCESS));
		assertFalse(failureLoginResponse.getBoolean(WebParams.SUCCESS));
		assertTrue(successLoginResponse.getBoolean(WebParams.SUCCESS));
	}

}