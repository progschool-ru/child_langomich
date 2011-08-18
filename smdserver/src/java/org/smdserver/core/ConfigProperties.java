package org.smdserver.core;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.smdserver.jsp.ILink;
import org.smdserver.jsp.LinkCreator;
import org.smdserver.jsp.SmdUrl;

class ConfigProperties implements IConfigProperties
{
	private static final String ABS_PATH_PREFIX = "link.path.";
	private static final String DEFAULT_PAGE_KEY = "pages.default.page.handler";
	private static final String HANDLER_KEY = ".handler";
	private static final String MAIN_TEMPLATE_KEY = ".mainTemplate";
	private static final String MENU_ITEMS_KEY = "items";
	private static final String MENU_PREFIX = "menu.";
	private static final String NEEDS_AUTHORITY_KEY = ".needsAuthority";
	private static final String PAGES_PREFIX = "pages.";
	private static final String TEXT_KEY = ".text";
	private static final String TITLE_KEY = ".title";
	private static final String URL_KEY = ".url";
	private static final String WEB_CHARSET_KEY = "web.charset";
	
	private String basePath;
	private ResourceBundle rb;
	private String serverConfigKey;
	
	public ConfigProperties(String configFile, String serverConfigKey,
			                String basePath)
	{
		this.basePath = basePath;
		this.rb = ResourceBundle.getBundle(configFile);
		this.serverConfigKey = serverConfigKey;
	}
	
	@Deprecated
	public ResourceBundle getConfigResource()
	{
		return rb;
	}
	
	public void close()
	{
		rb = null;
		serverConfigKey = null;
	}
	
	public String getWebCharset()
	{
		return rb.getString(WEB_CHARSET_KEY);
	}
	
	
	//IJSPConfig implementation
	public String getServletPrefix (String servlet)
	{
		return rb.getString(ABS_PATH_PREFIX + servlet);
	}
	
	public boolean containsMainTemplate(String page)
	{
		return rb.containsKey(PAGES_PREFIX + page + MAIN_TEMPLATE_KEY);
	}
	
	public String getMainTemplate(String page)
	{
		return rb.getString(PAGES_PREFIX + page + MAIN_TEMPLATE_KEY);
	}
	
	public boolean needsAuthority(String page)
	{
		return rb.containsKey(PAGES_PREFIX + page + NEEDS_AUTHORITY_KEY);
	}
	
	public String getTitle(String page)
	{
		String key = PAGES_PREFIX + page + TITLE_KEY;
		return rb.containsKey(key) ? rb.getString(key) : null;		
	}
	

	public List<ILink> createMenu(String menu, SmdUrl currentLink)
	{
		String menuPrefix = MENU_PREFIX + menu + ".";
		String [] items = rb.getString(menuPrefix + MENU_ITEMS_KEY).split(",");
		List<ILink> list = new ArrayList<ILink>();
		LinkCreator creator = new LinkCreator();

		for(String item : items)
		{
			String url = rb.getString(menuPrefix + item + URL_KEY);
			String text = rb.getString(menuPrefix + item + TEXT_KEY);
			list.add(creator.createLink(url, text, currentLink, basePath, null));
		}
		return list;
	}
	
	public boolean containsHandler (String page)
	{
		return rb.containsKey(PAGES_PREFIX + page + HANDLER_KEY);
	}
	
	public String getHandler (String page)
	{
		return rb.getString(PAGES_PREFIX + page + HANDLER_KEY);
	}
	
	public String getDefaultHandler ()
	{
		return rb.getString(DEFAULT_PAGE_KEY);
	}
}
