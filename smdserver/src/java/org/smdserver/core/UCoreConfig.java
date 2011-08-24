package org.smdserver.core;

import java.util.ResourceBundle;
import org.smdserver.core.small.ICoreConfig;
import org.smdserver.core.small.BaseConfig;

class UCoreConfig extends BaseConfig implements ICoreConfig
{
	public UCoreConfig(ResourceBundle rb)
	{
		super(rb);
	}
	
	public String getSecret()
	{
		return getString("hash.secret");
	}
}
