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
		String url = getActionUrl();
		wc = new WebConversation();
		req = new GetMethodWebRequest(url + WebActions.LOGIN);
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
		req.setParameter(WebParams.LOGIN, LOGIN);
		req.setParameter(WebParams.PASSWORD, "incorrect" + PASSWORD);
		WebResponse resp = wc.getResource(req);

		String text = resp.getText();
		JSONObject json = new JSONObject(text);
		assertFalse(json.getBoolean(WebParams.SUCCESS));
	}

	@Test
	public void testCorrectPassword() throws IOException, JSONException
	{
		req.setParameter(WebParams.LOGIN, LOGIN);
		req.setParameter(WebParams.PASSWORD, PASSWORD);
		WebResponse resp = wc.getResource(req);

		String text = resp.getText();
		JSONObject json = new JSONObject(text);
		assertTrue(json.getBoolean(WebParams.SUCCESS));
	}

	@Test
	public void testPasswordIsNull() throws IOException, JSONException, SAXException
	{
		req.setParameter(WebParams.LOGIN, LOGIN);
		JSONObject json = getJSONResource(wc, req);
		assertFalse(json.getBoolean(WebParams.SUCCESS));
	}

	@Test
	public void testAllParamsAreNull() throws IOException, JSONException, SAXException
	{
		JSONObject json = getJSONResource(wc, req);
		assertFalse(json.getBoolean(WebParams.SUCCESS));
	}
}