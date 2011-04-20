package org.smdserver.actionssystem;

import org.smdserver.actionssystem.IActionsFactory;
import org.smdserver.actionssystem.IAction;
import org.smdserver.FirstAction;
import org.smdserver.actionssystem.ActionsFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smdserver.SecondAction;

public class SmdServlet extends HttpServlet
{
	private IActionsFactory factory;
	private Class defaultActionClass;

	public SmdServlet()
	{
		super();

		factory = new ActionsFactory();
		factory.registerAction("first", FirstAction.class);
		factory.registerAction("second", SecondAction.class);

		defaultActionClass = FirstAction.class;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
											throws ServletException, IOException
	{
//		try
//		{
			IAction action = factory.createAction(getActionName(request));

			if(action == null)
			{
				try
				{
					action = (IAction) defaultActionClass.newInstance();
				}
				catch(Exception e)
				{
					throw new ServletException(e.getMessage());
				}
			}

			String url = action.perform(request, response);

			if (url != null)
			{
				getServletContext().getRequestDispatcher(url).forward(request, response);
			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
	}

	private String getActionName(HttpServletRequest request)
	{
		String pathInfo = request.getPathInfo();
		if(pathInfo == null)
			return null;
		
		return request.getPathInfo().substring(1);
	}
}