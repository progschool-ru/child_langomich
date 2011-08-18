package org.smdserver.core;

import java.util.ResourceBundle;
import org.smdserver.jsp.IJSPConfig;

public interface ISmdCore 
{
	@Deprecated
	ResourceBundle getConfigResource();
	IJSPConfig getJSPConfig();
}
