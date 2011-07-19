package org.smdserver;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smdserver.actionssystem.SessionKeys;
import org.smdserver.core.SmdConfigBean;

public class PagesServlet extends HttpServlet
{
	private static final String LOGIN_PAGE = "login";
	private static final String PAGE_404 = "words";
	private static final String PAGES_KEY = "pages.";
	private static final String MAIN_TEMPLATE_KEY = ".mainTemplate";
	private static final String NEEDS_AUTHORITY_KEY = ".needsAuthority";
	private static final String MAIN_TEMPLATE_ATTRIBUTE = "mainTemplate";

	@Override
	public void service (HttpServletRequest request, HttpServletResponse response)
											throws ServletException, IOException
	{
		ResourceBundle rb = ResourceBundle.getBundle(SmdConfigBean.CONFIG_RESOURCE);
		String page = getPageName(request);

		if(page == null)
		{
			response.sendRedirect(request.getServletPath().split("/")[2] + "/" + PAGE_404);
			return;
		}
		
		String pagePrefix = PAGES_KEY + page;

		if(!rb.containsKey(pagePrefix + MAIN_TEMPLATE_KEY))
		{
			response.sendRedirect(PAGE_404);
			return;
		}

		if(rb.containsKey(pagePrefix + NEEDS_AUTHORITY_KEY) && !isLoggedIn(request))
		{
			response.sendRedirect(LOGIN_PAGE);
			return;
		}

		String mainTemplate = rb.getString(pagePrefix + MAIN_TEMPLATE_KEY);
		String url = "/main.jsp";
		request.setAttribute(MAIN_TEMPLATE_ATTRIBUTE, mainTemplate);
		getServletContext().getRequestDispatcher(url).forward(request, response);
	}

	private String getPageName (HttpServletRequest request)
	{
		String pathInfo = request.getPathInfo();
		if(pathInfo == null)
			return null;

		return request.getPathInfo().substring(1);
	}

	private boolean isLoggedIn (HttpServletRequest request)
	{
		String login = (String)request.getSession().getAttribute(SessionKeys.CURRENT_LOGIN);
		return login != null;
	}
}