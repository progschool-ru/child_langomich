package org.smdserver;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smdserver.actionssystem.Action;
import org.smdserver.actionssystem.IAction;

public class FirstAction extends Action implements IAction
{
	@Override
	public String perform (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		return "/"+"FirstAction.jsp";
        }

	protected String doAction (HttpServletRequest request){return null;}
}
