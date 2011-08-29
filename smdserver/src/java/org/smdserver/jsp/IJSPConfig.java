package org.smdserver.jsp;

import java.util.List;
import java.util.Locale;

public interface IJSPConfig 
{
	public String getBasePath();
	public String getActionPath();
	public String getPagePath();
	String getServletPrefix(String servlet);

	boolean containsMainTemplate(String page);
	String getMainTemplate(String page);
	boolean containsHandler(String page);
	String getHandler(String page);
	String getDefaultHandler();
	
	boolean needsAuthority(String page);
	List<ILink> createMenu(String menu, SmdUrl currentLink, Locale locale);
	String getTitle(String page);
	String getWebCharset();
}
