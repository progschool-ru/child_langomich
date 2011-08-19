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
import org.smdserver.users.IUsersStorage;

public class PagesServlet extends HttpServlet
{
	public static final String PAGE_BEAN_KEY = "pageBean";

	private static final String LOGIN_PAGE = "login";
	private static final String PAGE_404 = "words";

	private static final String LOGGED_MENU_KEY = "main";
	private static final String ANONYMUS_MENU_KEY = "anonymus";
	
	private static final String PAGE_ACTION = "page";

	private ISmdCore core;
	private IUsersStorage usersStorage;
	
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
		
		PagesBean bean = new PagesBean(core, getUserId(request));

		bean.setDBConfig(core.getDBConfig());
		bean.setJSPConfig(config);
		bean.setMainTemplate(config.getMainTemplate(page));
		bean.setTitle(config.getTitle(page));
		SmdUrl currentUrl = new SmdUrl(PAGE_ACTION, page);
		bean.setCurrentUrl(currentUrl);
		bean.setWebCharset(config.getWebCharset());
		
		List<ILink> links = config.createMenu( 
				      isLoggedIn(request) ? LOGGED_MENU_KEY : ANONYMUS_MENU_KEY,
					  currentUrl);
		bean.setMenuLinks(links);
		
		request.setAttribute(PAGE_BEAN_KEY, bean);

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
	
	private String getUserId (HttpServletRequest request)
	{
		String login = (String)request.getSession().getAttribute(SessionKeys.CURRENT_LOGIN);
		
		if(login == null)
		{
			return null;
		}
		
		if(usersStorage == null)
		{
			usersStorage = core.createUsersStorage();
		}
		
		return usersStorage.getUserByLogin(login).getUserId();
	}
}