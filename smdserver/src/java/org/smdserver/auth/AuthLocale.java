package org.smdserver.auth;

import java.util.ResourceBundle;
import org.smdserver.core.small.ResourceBundleBased;

public class AuthLocale extends ResourceBundleBased implements IAuthLocale
{
	public AuthLocale(ResourceBundle rb)
	{
		super(rb);
	}
	
	public String getConfirmBody()
	{
		return getString("mail.register.confirm.body");
	}
	
	public String getConfirmSubject()
	{
		return getString("mail.register.confirm.subject");
	}

	public String getNotifyAdminAboutConfirmationSubject()
	{
		return getString("mail.register.created.notifyAdmin.subject");
	}

	public String getNotifyAdminAboutConfirmationBody()
	{
		return getString("mail.register.created.notifyAdmin.body");
	}

	public String getNotifyUserAboutConfirmationSubject()
	{
		return getString("mail.register.created.subject");
	}
	
	public String getNotifyUserAboutConfirmationBody()
	{
		return getString("mail.register.created.body");
	}

	public String getNotifyAdminAboutRefusingSubject()
	{
		return getString("mail.register.refused.notifyAdmin.subject");
	}

	public String getNotifyAdminAboutRefusingBody()
	{
		return getString("mail.register.refused.notifyAdmin.body");
	}

	public String getNotifyAdminAboutRegistrationRequestSubject()
	{
		return getString("mail.register.confirm.notifyAdmin.subject");
	}
	
	public String getNotifyAdminAboutRegistrationRequestBody()
	{
		return getString("mail.register.confirm.notifyAdmin.body");
	}
	
	public String getSubjectPrefix()
	{
		return getString("mail.subjectPrefix");
	}
}
