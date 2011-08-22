package org.smdserver.mail;

import java.util.ResourceBundle;
import org.smdserver.util.IClosable;

public class MailConfig implements IMailConfig, IClosable
{
	private ResourceBundle rb;
	
	public MailConfig(ResourceBundle rb)
	{
		this.rb = rb;
	}
	
	public boolean isEnabled()
	{
		return "true".equals(rb.getString("mail.enabled"));
	}

	public String getSMTPHost()
	{
		return rb.getString("mail.smtpHost");
	}
	
	public boolean isAuth()
	{
		return "true".equals(rb.getString("mail.isAuth"));
	}
	
	public String getLogin()
	{
		return rb.getString("mail.login");
	}
	
	public String getPassword()
	{
		return rb.getString("mail.password");
	}
	
	public String getFrom()
	{
		return rb.getString("mail.from");
	}
	
	public boolean close()
	{
		return true;
	}
}
