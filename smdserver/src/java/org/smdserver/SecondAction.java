package org.smdserver;

import org.smdserver.actionssystem.IAction;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smdserver.actionssystem.Action;

public class SecondAction extends Action
{
	public String perform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		PrintWriter writer = response.getWriter();
		writer.println("It's a second action!");
		writer.close();

		return null;
	}
}