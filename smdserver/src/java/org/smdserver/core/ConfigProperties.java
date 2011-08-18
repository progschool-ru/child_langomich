package org.smdserver.core;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.smdserver.jsp.ILink;
import org.smdserver.jsp.LinkCreator;
import org.smdserver.jsp.SmdUrl;

//TODO: (1.high) Make it internal.
public class ConfigProperties implements IConfigProperties
{
	private static final String HANDLER_KEY = ".handler";
	private static final String MAIN_TEMPLATE_KEY = ".mainTemplate";
	private static final String PAGES_PREFIX = "pages.";
	
	private String basePath;
	private ResourceBundle rb;
	private ResourceBundle serverRB;
	
	public ConfigProperties(String configFile, String serverConfigKey,
			                String basePath)
	{
		this.basePath = basePath;
		this.rb = ResourceBundle.getBundle(configFile);
		
		String serverFile = rb.getString(serverConfigKey);
		this.serverRB = ResourceBundle.getBundle(serverFile);
	}
	
	@Deprecated
	public ResourceBundle getConfigResource()
	{
		return rb;
	}
	
	public void close()
	{
		rb = null;
		serverRB = null;
		basePath = null;
	}
	
	public String getWebCharset()
	{
		return rb.getString("web.charset");
	}
	
	
	//IJSPConfig implementation
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
		return rb.getString("pages.default.page.handler");
	}
	
	
	//IDBConfig implementation
	public String getTablesPrefix()
	{
		return serverRB.getString("db.tablesPrefix");
	}
	
	public String getDBUrl()
	{
		return serverRB.getString("db.url");
	}
	
	public String getDBUser()
	{
		return serverRB.getString("db.user");
	}
	
	public String getDBPassword()
	{
		return serverRB.getString("db.password");
	}
	
	//IMaintenanceConfig implementation
	public boolean isMaintenanceAllowed()
	{
		return "true".equals(serverRB.getString("maintenance.allowed"));
	}
	
	public String getMaintenancePassword()
	{
		return serverRB.getString("maintenance.password");
	}
}
