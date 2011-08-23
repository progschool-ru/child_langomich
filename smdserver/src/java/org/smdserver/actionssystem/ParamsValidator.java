package org.smdserver.actionssystem;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

public class ParamsValidator 
{
	private static final String EMAIL_REGEX = "[a-z0-9._%-]+@[a-z0-9.-]+\\.[a-z]{2,4}";
	private HttpServletRequest request;
	
	public ParamsValidator(HttpServletRequest request)
	{
		this.request = request;
	}
	
	public String getNotEmpty(String paramName) throws ActionException
	{
		String param = request.getParameter(paramName);
		
		if(param == null || param.isEmpty())
		{	
			throwError(ActionException.MUST_NOT_BE_EMPTY, paramName, param);
		}
		
		return param;
	}
	
	public String getEmail(String paramName) throws ActionException
	{
		String param = getNotEmpty(paramName).toLowerCase(Locale.FRENCH);
		if(!param.matches(EMAIL_REGEX))
		{
			throwError(ActionException.MUST_BE_VALID_EMAIL, paramName, param);
		}
		return param;
	}
	
	private void throwError(String template, String paramName, String param) throws ActionException
	{
		throw new ActionException(String.format(template, paramName, param),
				                  String.format(template, paramName, "--"));
	}
}
