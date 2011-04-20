package org.smdserver.actionssystem;

import org.smdserver.FirstAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smdserver.LoginAction;
import org.smdserver.SecondAction;
import org.smdserver.users.UsersStorage;

public class SmdServlet extends HttpServlet
{
	private IActionsFactory factory;
	private Class defaultActionClass;

	@Override
	public void init() throws ServletException
	{
		super.init();
		
		UsersStorage usersStorage = new UsersStorage();
		ISmdServletContext context = new SmdServletContext(usersStorage);

		factory = new ActionsFactory(context);

		factory.registerAction("first", FirstAction.class);
		factory.registerAction("second", SecondAction.class);
		factory.registerAction("login", LoginAction.class);

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