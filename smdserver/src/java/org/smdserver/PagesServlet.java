package org.smdserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smdserver.actionssystem.SessionKeys;
import org.smdserver.core.SmdConfigBean;
import org.smdserver.jsp.ILink;
import org.smdserver.jsp.LinkCreator;
import org.smdserver.jsp.SmdLink;

public class PagesServlet extends HttpServlet
{
	private static final String LOGIN_PAGE = "login";
	private static final String PAGE_404 = "words";

	private static final String MENU_PREFIX = "menu.";
	private static final String PAGES_PREFIX = "pages.";

	private static final String MAIN_TEMPLATE_KEY = "mainTemplate";
	private static final String MENU_KEY = "menu";
	private static final String NEEDS_AUTHORITY_KEY = "needsAuthority";
	private static final String TITLE_KEY = "title";
	private static final String LOGGED_MENU_KEY = "main";
	private static final String ANONYMUS_MENU_KEY = "anonymus";
	private static final String MENU_ITEMS_KEY = "items";
	private static final String URL_KEY = ".url";
	private static final String TEXT_KEY = ".text";
	private static final String CURRENT_LINK_KEY = "currentLink";

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
		
		String pagePrefix = PAGES_PREFIX + page + ".";

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
		request.setAttribute(MAIN_TEMPLATE_KEY, mainTemplate);
		String title = rb.containsKey(pagePrefix + TITLE_KEY) ? rb.getString(pagePrefix + TITLE_KEY) : null;
		request.setAttribute(TITLE_KEY, title);
		SmdLink currentLink = new SmdLink("page", page, rb, null);
		request.setAttribute(CURRENT_LINK_KEY, currentLink);
		
		List<ILink> links = createMenu(rb, 
				      isLoggedIn(request) ? LOGGED_MENU_KEY : ANONYMUS_MENU_KEY,
					  currentLink);
		request.setAttribute(MENU_KEY, links);

		String url = "/main.jsp";
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

	private List<ILink> createMenu(ResourceBundle rb, String menu, SmdLink currentLink)
	{
		String menuPrefix = MENU_PREFIX + menu + ".";
		String [] items = rb.getString(menuPrefix + MENU_ITEMS_KEY).split(",");
		List<ILink> list = new ArrayList<ILink>();
		LinkCreator creator = new LinkCreator();
		String basePath = getServletContext().getContextPath();

		for(String item : items)
		{
			String url = rb.getString(menuPrefix + item + URL_KEY);
			String text = rb.getString(menuPrefix + item + TEXT_KEY);
			list.add(creator.createLink(url, text, rb, currentLink, basePath));
		}
		return list;
	}
}