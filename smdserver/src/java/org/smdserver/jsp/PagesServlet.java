package org.smdserver.jsp;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smdserver.actionssystem.SessionKeys;
import org.smdserver.core.CoreInstance;
import org.smdserver.core.ISmdCore;

public class PagesServlet extends HttpServlet
{
	private static final String LOGIN_PAGE = "login";
	private static final String PAGE_404 = "words";

	private static final String MAIN_TEMPLATE_KEY = "mainTemplate";
	private static final String MENU_KEY = "menu";
	private static final String TITLE_KEY = "title";
	private static final String LOGGED_MENU_KEY = "main";
	private static final String ANONYMUS_MENU_KEY = "anonymus";
	private static final String CURRENT_LINK_KEY = "currentLink";
	private static final String WEB_CHARSET_KEY = "web.charset";

	private ISmdCore core;
	
	@Override
	public void init() throws ServletException
	{
		super.init();
		core = CoreInstance.getInstance(getServletContext());
	}

	@Override
	public void service (HttpServletRequest request, HttpServletResponse response)
											throws ServletException, IOException
	{
		IJSPConfig config = core.getJSPConfig();
		String page = getPageName(request);

		if(page == null)
		{
			response.sendRedirect(request.getServletPath().split("/")[2] + "/" + PAGE_404);
			return;
		}

		if(!config.containsMainTemplate(page))
		{
			response.sendRedirect(PAGE_404);
			return;
		}

		if(config.needsAuthority(page) && !isLoggedIn(request))
		{
			response.sendRedirect(LOGIN_PAGE);
			return;
		}

		request.setAttribute(MAIN_TEMPLATE_KEY, config.getMainTemplate(page));
		request.setAttribute(TITLE_KEY, config.getTitle(page));
		SmdUrl currentUrl = new SmdUrl("page", page);
		request.setAttribute(CURRENT_LINK_KEY, currentUrl);
		request.setAttribute(WEB_CHARSET_KEY, config.getWebCharset());
		
		List<ILink> links = config.createMenu( 
				      isLoggedIn(request) ? LOGGED_MENU_KEY : ANONYMUS_MENU_KEY,
					  currentUrl);
		request.setAttribute(MENU_KEY, links);

		String url = config.containsHandler(page) 
						? config.getHandler(page)
						: config.getDefaultHandler();
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