package org.smdserver.jsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.smdserver.core.small.ResourceBundleBased;

public class JSPConfig extends ResourceBundleBased implements IJSPConfig
{
	private static final String HANDLER_KEY = ".handler";
	private static final String MAIN_TEMPLATE_KEY = ".mainTemplate";
	private static final String PAGES_PREFIX = "pages.";
	
	private String basePath;
	private String localeResource;
		
	public JSPConfig(ResourceBundle rb, String basePath, String localeResource)
	{
		super(rb);
		this.basePath = basePath;
		this.localeResource = localeResource;
	}
	
	public String getWebCharset()
	{
		return getString("web.charset");
	}
	
	public String getBasePath()
	{
		return basePath;
	}

	public String getActionPath()
	{
		return getServletPrefix("action");
	}

	public String getPagePath()
	{
		return getServletPrefix("page");
	}
	
	public String getServletPrefix (String servlet)
	{
		return getString("link.path." + servlet);
	}
	
	public boolean containsMainTemplate(String page)
	{
		return containsKey(PAGES_PREFIX + page + MAIN_TEMPLATE_KEY);
	}
	
	public String getMainTemplate(String page)
	{
		return getString(PAGES_PREFIX + page + MAIN_TEMPLATE_KEY);
	}
	
	public boolean needsAuthority(String page)
	{
		return containsKey(PAGES_PREFIX + page + ".needsAuthority");
	}
	
	public String getTitle(String page)
	{
		String key = PAGES_PREFIX + page + ".title";
		return containsKey(key) ? getString(key) : null;		
	}
	

	public List<ILink> createMenu(String menu, SmdUrl currentLink, Locale locale)
	{
		ResourceBundle rb = ResourceBundle.getBundle(localeResource, locale);
		String menuPrefix = "menu." + menu + ".";
		String [] items = getString(menuPrefix + "items").split(",");
		List<ILink> list = new ArrayList<ILink>();
		LinkCreator creator = new LinkCreator();

		for(String item : items)
		{
			String url = getString(menuPrefix + item + ".url");
			String textKey = getString(menuPrefix + item + ".text");
			String text = rb.getString(textKey);
			list.add(creator.createLink(url, text, currentLink, null));
		}
		return list;
	}
	
	public boolean containsHandler (String page)
	{
		return containsKey(PAGES_PREFIX + page + HANDLER_KEY);
	}
	
	public String getHandler (String page)
	{
		return getString(PAGES_PREFIX + page + HANDLER_KEY);
	}
	
	public String getDefaultHandler ()
	{
		return getString("pages.default.page.handler");
	}
	
	public String getYandexMetrikaId()
	{
		return getString("yandex.metrika.id");
	}
}
