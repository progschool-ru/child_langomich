package org.smdserver.core.small;

import java.util.ResourceBundle;

abstract public class ResourceBundleBased 
{
	private ResourceBundle rb;
	
	protected ResourceBundleBased(ResourceBundle rb)
	{
		this.rb = rb;
	}
	
	protected String getString(String key)
	{
		return rb.getString(key);
	}
	
	protected boolean containsKey(String key)
	{
		return rb.containsKey(key);
	}
	
	protected boolean getBoolean(String key)
	{
		return "true".equals(rb.getString(key));
	}
}
