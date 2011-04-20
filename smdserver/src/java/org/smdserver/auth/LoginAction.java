package org.smdserver.auth;

import org.smdserver.actionssystem.SessionKeys;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.actionssystem.Action;
import org.smdserver.users.UsersStorage;

public class LoginAction extends Action
{
	public String perform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String login = request.getParameter(ActionParams.LOGIN);
		String password = request.getParameter(ActionParams.PASSWORD).toString();
		UsersStorage storage = getServletContext().getUsersStorage();

		boolean success = storage.checkPassword(login, password);

		if(success)
		{
			request.getSession().setAttribute(SessionKeys.CURRENT_LOGIN, login);
		}
		else
		{
			request.getSession().setAttribute(SessionKeys.CURRENT_LOGIN, null);
		}

		PrintWriter writer = response.getWriter();
		writer.println( success ? "success" : "failure");
		writer.close();

		return null;
	}
}
