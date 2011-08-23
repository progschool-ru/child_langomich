package org.smdserver.core;

import java.util.ResourceBundle;

class CoreConfig implements ICoreConfig
{
	private ResourceBundle rb;
	
	public CoreConfig(ResourceBundle rb)
	{
		this.rb = rb;
	}
	
	public String getSecret()
	{
		return rb.getString("hash.secret");
	}
}
