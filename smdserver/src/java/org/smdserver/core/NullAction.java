package org.smdserver.core;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smdserver.actionssystem.IAction;

public class NullAction extends SmdAction implements IAction
{
	@Override
	public String perform (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String queryString = request.getQueryString();
		StringBuilder message = new StringBuilder();
		message.append(request.getRemoteAddr());
		message.append(" calls incorrect action: ");
		message.append(request.getRequestURL());
		if(queryString != null)
		{
			message.append('?');
			message.append(request.getQueryString());
		}
		log(message.toString());
		return null;
    }

	protected String doAction (HttpServletRequest request){return null;}
}
