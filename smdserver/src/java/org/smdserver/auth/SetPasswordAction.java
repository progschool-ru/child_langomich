package org.smdserver.auth;

import org.smdserver.actionssystem.SessionKeys;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smdserver.actionssystem.Action;
import org.smdserver.actionssystem.ActionParams;

public class SetPasswordAction extends Action
{
	public String perform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String login = (String)request.getSession().getAttribute(SessionKeys.CURRENT_LOGIN);
		String password = request.getParameter(ActionParams.PASSWORD);

		boolean success = (login != null && password != null && !password.isEmpty());

		if(success)
		{
			getServletContext().getUsersStorage().setPassword(login, password);
		}

		PrintWriter writer = response.getWriter();
		writer.println(success ? "success" : "failure");
		writer.close();

		return null;
	}
}