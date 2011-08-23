package org.smdserver.auth;

import java.util.ResourceBundle;

public class RegisterConfig implements IRegisterConfig
{
	private ResourceBundle rb;
	
	public RegisterConfig(ResourceBundle rb)
	{
		this.rb = rb;
	}
	
	public ConfirmationType getConfirmationType()
	{
		String name = rb.getString("registration.confirmation");
		return ConfirmationType.getType(name);
	}
	
	public boolean shouldNotifyAdmin()
	{
		return "true".equals(rb.getString("registration.notifyAdmin"));
	}
	
	public String getAdminEmail()
	{
		return rb.getString("registration.adminEmail");
	}
}