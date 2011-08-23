package org.smdserver.actionssystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import static org.smdserver.actionssystem.ActionParams.*;
import com.ccg.util.JavaString;

public abstract class Action implements IAction
{
	private Map<String, Object> map = new HashMap<String, Object>();
	private String redirectUrl;

	abstract protected String doAction (HttpServletRequest request);

	public String perform (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String url = null;

		boolean success = false;
		if(validateParams(request) && validateContext(request))
		{
			try
			{
				url = doAction(request);
				success = true && (!map.containsKey(SUCCESS) || (Boolean)map.get(SUCCESS));
			}
			catch(Exception e)
			{
				log(e);
			}
		}

		setAnswerParam(SUCCESS, success);

		if (url == null)
		{
			redirectUrl = extractRedirectUrl(success, request);

			if (redirectUrl == null)
			{
				JSONObject object = new JSONObject(map);

				PrintWriter writer = response.getWriter();
				writer.print(JavaString.encode(object.toString()));
				writer.close();
			}
		}
		return url;
	}

	public String getRedirectUrl()
	{
		return redirectUrl;
	}

	protected String getRedirectParamsURI(HttpServletRequest request)
	{
		String [] keys = {REDIRECT, REDIRECT_FAILURE, REDIRECT_SUCCESS};

		StringBuilder sb = new StringBuilder();

		for(String key : keys)
		{
			String value = request.getParameter(key);
			if(value != null)
			{
				if(sb.length() > 0)
				{
					sb.append('&');
				}
				sb.append(key);
				sb.append('=');
				sb.append(value);
			}
		}

		return sb.toString();
	}

	protected void setAnswerParam (String key, Object value)
	{
		map.put(key, value);
	}

	protected boolean validateParams (HttpServletRequest request)
	{
		return true;
	}

	protected boolean validateContext (HttpServletRequest request)
	{
		return true;
	}

	protected void log(String message)
	{
	}
	
	protected void log(Throwable e)
	{
	}

	private String extractRedirectUrl(boolean success, HttpServletRequest request)
	{
		String redirect = request.getParameter(REDIRECT);
		String redirectFailure = request.getParameter(REDIRECT_FAILURE);
		String redirectSuccess = request.getParameter(REDIRECT_SUCCESS);

		if(success && redirectSuccess != null)
		{
			return redirectSuccess;
		}

		if(!success && redirectFailure != null)
		{
			return redirectFailure;
		}

		return redirect;
	}
}
