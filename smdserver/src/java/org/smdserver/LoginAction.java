package org.smdserver;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smdserver.actionssystem.Action;
import org.smdserver.users.UsersStorage;

public class LoginAction extends Action
{
	public String perform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String login = request.getParameter("login");
		String password = request.getParameter("password").toString();
		UsersStorage storage = getServletContext().getUsersStorage();

		PrintWriter writer = response.getWriter();
		writer.println( storage.checkPassword(login, password) ? "success" : "failure");
		writer.close();

		return null;
	}
}
