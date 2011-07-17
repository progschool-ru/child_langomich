package org.smdserver.core;

import java.util.ResourceBundle;

/*
 * We are going to use this class in JSP pages.
 * There are a number of pages, that's why,
 * we want to have quick access to configuration.
 *
 * TODO: (3.low) Find way to use standard servlet configs.
 */
public class SmdConfigBean implements ISmdConfig
{
	public static final String CONFIG_RESOURCE   = "org.smdserver.config";

	private static final String WEB_CHARSET_KEY  = "web.charset";
	private static final String ACTIONS_PATH_KEY = "path.actions";

	private static ISmdConfig instance;

	private ResourceBundle bundle;

	public SmdConfigBean()
	{
		bundle = ResourceBundle.getBundle(CONFIG_RESOURCE);
	}

	public static ISmdConfig getInstance()
	{
		if(instance == null)
		{
			instance = new SmdConfigBean();
		}
		return instance;
	}

	public String getWebCharset()
	{
		return bundle.getString(WEB_CHARSET_KEY);
	}
	
	public String getActionsPath()
	{
		return bundle.getString(ACTIONS_PATH_KEY);
	}
}
