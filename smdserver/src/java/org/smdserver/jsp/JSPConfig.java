package org.smdserver.jsp;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JSPConfig implements IJSPConfig
{
	private static final String HANDLER_KEY = ".handler";
	private static final String MAIN_TEMPLATE_KEY = ".mainTemplate";
	private static final String PAGES_PREFIX = "pages.";
	
	private String basePath;
	private ResourceBundle rb;
	
	public JSPConfig(String configFile, String basePath)
	{
		this.basePath = basePath;
		this.rb = ResourceBundle.getBundle(configFile);
	}
	
	public JSPConfig(ResourceBundle rb, String basePath)
	{
		this.basePath = basePath;
		this.rb = rb;
	}
	
	public String getWebCharset()
	{
		return rb.getString("web.charset");
	}
	
	public String getBasePath()
	{
		return basePath;
	}

	public String getActionPath()
	{
		return getServletPrefix("action");
	}
	
	public String getServletPrefix (String servlet)
	{
		return rb.getString("link.path." + servlet);
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
		return rb.containsKey(PAGES_PREFIX + page + ".needsAuthority");
	}
	
	public String getTitle(String page)
	{
		String key = PAGES_PREFIX + page + ".title";
		return rb.containsKey(key) ? rb.getString(key) : null;		
	}
	

	public List<ILink> createMenu(String menu, SmdUrl currentLink)
	{
		String menuPrefix = "menu." + menu + ".";
		String [] items = rb.getString(menuPrefix + "items").split(",");
		List<ILink> list = new ArrayList<ILink>();
		LinkCreator creator = new LinkCreator();

		for(String item : items)
		{
			String url = rb.getString(menuPrefix + item + ".url");
			String text = rb.getString(menuPrefix + item + ".text");
			list.add(creator.createLink(url, text, currentLink, null));
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
		return rb.getString("pages.default.page.handler");
	}
}
