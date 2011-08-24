package org.smdserver.actionssystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import com.ccg.util.JavaString;
import org.smdserver.core.small.ISmdLogger;
import org.smdserver.core.small.SmdException;

import static org.smdserver.actionssystem.ActionParams.*;

public abstract class Action implements IAction
{
	private Map<String, Object> map = new HashMap<String, Object>();
	private String redirectUrl;
	private String answerMessage;
	private boolean answerSuccess = true;

	abstract protected String doAction (HttpServletRequest request) throws SmdException;
	abstract protected ISmdLogger getLogger();


	public String perform (HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		String url = null;

		boolean success = false;
		try
		{
			if(validateParams(request) && validateContext(request))
			{
				url = doAction(request);
				success = true && answerSuccess;
			}
		}
		catch(SmdException e)
		{
			log(e);
			setAnswerMessage(e.getPublicMessage());
			success = false;
		}
		catch(Exception e)
		{
			log(e);
			setAnswerSuccess(false);
			setAnswerMessage(SmdException.ERROR);
		}

		setAnswerParam(SUCCESS, success);
		
		if(answerMessage != null)
		{
			setAnswerParam(ActionParams.MESSAGE, answerMessage);
		}

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
	
	protected void setAnswerSuccess (boolean value)
	{
		this.answerSuccess = value;
	}
	
	protected void setAnswerMessage (String value)
	{
		this.answerMessage = value;
	}

	protected void setAnswerParam (String key, Object value)
	{
		map.put(key, value);
	}

	protected boolean validateParams (HttpServletRequest request) throws SmdException
	{
		return true;
	}

	protected boolean validateContext (HttpServletRequest request) throws SmdException
	{
		return true;
	}

	protected void log(String message)
	{
		if(getLogger() != null)
		{
			getLogger().log(message);
		}
	}
	
	protected void log(Throwable e)
	{
		if(getLogger() != null)
		{
			getLogger().log(e);
		}
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
