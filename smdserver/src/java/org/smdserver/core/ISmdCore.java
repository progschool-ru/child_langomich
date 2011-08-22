package org.smdserver.core;

import org.smdserver.jsp.IJSPConfig;

public interface ISmdCore 
{
	public IJSPConfig getJSPConfig();
	
	public ISmdServletContext createContext();
	public ISmdCoreFactory getFactory();
}
