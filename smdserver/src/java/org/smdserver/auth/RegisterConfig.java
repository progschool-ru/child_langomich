package org.smdserver.auth;

import java.util.ResourceBundle;
import org.smdserver.core.small.ResourceBundleBased;

public class RegisterConfig extends ResourceBundleBased implements IRegisterConfig
{
	public RegisterConfig(ResourceBundle rb)
	{
		super(rb);
	}
	
	public ConfirmationType getConfirmationType()
	{
		String name = getString("registration.confirmation");
		return ConfirmationType.getType(name);
	}
	
	public boolean shouldNotifyAdmin()
	{
		return getBoolean("registration.notifyAdmin");
	}
	
	public String getAdminEmail()
	{
		return getString("registration.adminEmail");
	}
}