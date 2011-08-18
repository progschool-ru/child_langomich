package org.smdserver.core;

import java.util.ResourceBundle;
import org.smdserver.jsp.IJSPConfig;

interface IConfigProperties extends IJSPConfig
{
	@Deprecated
	ResourceBundle getConfigResource();
	String getWebCharset();
}
