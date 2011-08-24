package org.smdserver.core;

import java.util.ResourceBundle;

class UCoreConfig implements ICoreConfig
{
	private ResourceBundle rb;
	
	public UCoreConfig(ResourceBundle rb)
	{
		this.rb = rb;
	}
	
	public String getSecret()
	{
		return rb.getString("hash.secret");
	}
}
