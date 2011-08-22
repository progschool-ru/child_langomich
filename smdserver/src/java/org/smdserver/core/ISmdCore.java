package org.smdserver.core;

public interface ISmdCore 
{	
	public ISmdServletContext createContext();
	public ISmdCoreFactory getFactory();
}
