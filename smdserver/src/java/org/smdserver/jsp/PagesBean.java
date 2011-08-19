package org.smdserver.jsp;

import java.util.List;
import org.smdserver.core.ISmdCore;
import org.smdserver.db.IDBConfig;
import org.smdserver.maintenance.IMaintenanceConfig;
import org.smdserver.words.IUserWords;
import org.smdserver.words.UserWords;

public class PagesBean
{
	private String mainTemplate;
	private String title;
	private SmdUrl currentUrl;
	private String webCharset;
	private List<ILink> menuLinks;
	private IDBConfig dbConfig;
	private IJSPConfig jspConfig;
	
	private ISmdCore core;
	private String userId;
	private IUserWords wordsBean;
	
	public PagesBean(ISmdCore core, String userId)
	{
		this.core = core;
		this.userId = userId;
	}
	
	public PagesBean()
	{
	}
	
	public IUserWords getUserWords()
	{
		if(wordsBean == null && core != null && userId != null)
		{
			System.out.println("Create wordsStorage");
			wordsBean = new UserWords(core, userId);
		}
		return wordsBean;
	}

	public SmdUrl getCurrentUrl() 
	{
		return currentUrl;
	}

	public void setCurrentUrl(SmdUrl currentUrl) 
	{
		this.currentUrl = currentUrl;
	}

	public String getMainTemplate() 
	{
		return mainTemplate;
	}

	public void setMainTemplate(String mainTemplate) 
	{
		this.mainTemplate = mainTemplate;
	}

	public List<ILink> getMenuLinks() 
	{
		return menuLinks;
	}

	public void setMenuLinks(List<ILink> menuLinks) 
	{
		this.menuLinks = menuLinks;
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}

	public String getWebCharset() 
	{
		return webCharset;
	}

	public void setWebCharset(String webCharset) 
	{
		this.webCharset = webCharset;
	}
	
	public IMaintenanceConfig getMaintenanceConfig()
	{
		return dbConfig;
	}
	
	public IDBConfig getDBConfig()
	{
		return dbConfig;
	}
	
	public void setDBConfig(IDBConfig dbConfig)
	{
		this.dbConfig = dbConfig;
	}
	
	public IJSPConfig getJSPConfig()
	{
		return jspConfig;
	}
	
	public void setJSPConfig(IJSPConfig jspConfig)
	{
		this.jspConfig = jspConfig;
	}
}
