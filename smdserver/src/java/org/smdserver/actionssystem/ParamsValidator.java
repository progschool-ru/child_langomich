package org.smdserver.actionssystem;

import com.ccg.util.JavaString;
import java.text.ParseException;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class ParamsValidator //TODO: (1.high) create interface and make this class internal
{
	private static final String EMAIL_REGEX = "[a-z0-9._%-]+@[a-z0-9.-]+\\.[a-z]{2,4}";
	private HttpServletRequest request;
	
	public ParamsValidator (HttpServletRequest request)
	{
		this.request = request;
	}
	
	public void checkNotNull (String paramName) throws ActionException
	{
		if(request.getParameter(paramName) == null)
		{
			throwError(ActionException.MUST_NOT_BE_NULL, paramName, null);
		}
	}
	
	public String getNotEmpty (String paramName) throws ActionException
	{
		String param = request.getParameter(paramName);
		
		if(param == null || param.isEmpty())
		{	
			throwError(ActionException.MUST_NOT_BE_EMPTY, paramName, param);
		}
		
		return param;
	}
	
	public String getEmail (String paramName) throws ActionException
	{
		String param = getNotEmpty(paramName).toLowerCase(Locale.FRENCH);
		if(!param.matches(EMAIL_REGEX))
		{
			throwError(ActionException.MUST_BE_VALID_EMAIL, paramName, param);
		}
		return param;
	}
	
	public JSONObject getJSONObject (String paramName) throws ActionException
	{
		String param = request.getParameter(paramName);
		if(param == null)
		{
			return null;
		}
		
		try
		{
			return new JSONObject(decode(paramName, param));
		}
		catch(JSONException e)
		{
			throwError(ActionException.MUST_BE_VALID_JSON_OBJECT, paramName, param, e);
			return null;
		}
	}
	
	private String decode(String paramName, String value) throws ActionException
	{
		try
		{
			return JavaString.decode(value);
		}
		catch(ParseException e)
		{
			throwError(ActionException.ENCODED_INCORRECTLY, paramName, value, e);
			return null;
		}
	}
	
	private void throwError (String template, String paramName, 
			                 String param) throws ActionException
	{
		throwError(template, paramName, param, null);
	}
	
	private void throwError (String template, String paramName, 
			                 String param, Throwable cause) throws ActionException
	{
		throw new ActionException(String.format(template, paramName, param),
				                  String.format(template, paramName, "--"), 
				                  cause);
	}
}
