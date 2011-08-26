package org.smdserver.core;

import java.util.ResourceBundle;
import org.smdserver.core.small.ResourceBundleBased;

class UTestConfig extends ResourceBundleBased implements ITestConfig
{
	public UTestConfig(ResourceBundle rb)
	{
		super(rb);
	}
	
	public String getTestUrl()
	{
		return getString("test.url");
	}
	
	public String getTestUrlAction()
	{
		return getString("test.url.action");
	}
}
