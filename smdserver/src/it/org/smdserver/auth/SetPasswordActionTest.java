package org.smdserver.auth;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
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
	public void testSuccessChange() throws IOException, JSONException, SAXException
	{
		WebRequest req = new GetMethodWebRequest(getActionUrl() + WebActions.LOGIN);
		req.setParameter(WebParams.LOGIN, LOGIN);
		req.setParameter(WebParams.PASSWORD, PASSWORD);

		WebResponse response = wc.getResponse(req);
		JSONObject loginResponse = new JSONObject(response.getText());

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