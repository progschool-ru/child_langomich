package org.smdserver.mail;

import java.util.ResourceBundle;
import org.smdserver.core.small.ResourceBundleBased;

public class MailConfig extends ResourceBundleBased implements IMailConfig
{
	public MailConfig(ResourceBundle rb)
	{
		super(rb);
	}
	
	public boolean isEnabled()
	{
		return getBoolean("mail.enabled");
	}

	public String getSMTPHost()
	{
		return getString("mail.smtpHost");
	}
	
	public boolean isAuth()
	{
		return getBoolean("mail.isAuth");
	}
	
	public String getLogin()
	{
		return getString("mail.login");
	}
	
	public String getPassword()
	{
		return getString("mail.password");
	}
	
	public String getFrom()
	{
		return getString("mail.from");
	}
	
	public String getReplyTo()
	{
		return getString("mail.replyTo");
	}
}
