package org.smdserver.actionssystem;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class SmdServlet extends HttpServlet
{
	private IActionsFactory factory;
	private String defaultActionName;
	
	abstract protected String getDefaultActionName ();
	abstract protected Map<String, Class> getActionsClasses ();
	abstract protected IActionsFactory createActionsFactory ();

	@Override
	public void init () throws ServletException
	{
		super.init();
		
		factory = createActionsFactory();
		factory.registerMap(getActionsClasses());

		defaultActionName = getDefaultActionName();
	}

	@Override
	public void service (HttpServletRequest request, HttpServletResponse response)
											throws ServletException, IOException
	{
			IAction action = factory.createAction(getActionName(request));

			if(action == null)
			{
				try
				{
					action = factory.createAction(defaultActionName);
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
			else if(action.getRedirectUrl() != null)
			{
				response.sendRedirect(action.getRedirectUrl());
			}
	}

	private String getActionName (HttpServletRequest request)
	{
		String pathInfo = request.getPathInfo();
		if(pathInfo == null)
			return null;
		
		return request.getPathInfo().substring(1);
	}
}