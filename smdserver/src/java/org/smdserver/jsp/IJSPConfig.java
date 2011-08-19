package org.smdserver.jsp;

import java.util.List;

public interface IJSPConfig 
{
	public String getActionPath();
	String getServletPrefix(String servlet);

	boolean containsMainTemplate(String page);
	String getMainTemplate(String page);
	boolean containsHandler(String page);
	String getHandler(String page);
	String getDefaultHandler();
	
	boolean needsAuthority(String page);
	List<ILink> createMenu(String menu, SmdUrl currentLink);
	String getTitle(String page);
	String getWebCharset();
}
