package org.smdserver.auth;

import org.smdserver.mail.IMailman;

class RegistrationMailer 
{
	IRegisterConfig config;
	IMailman mailman;
	
	public RegistrationMailer(IMailman mailman, IRegisterConfig config)
	{
		this.config = config;
		this.mailman = mailman;
	}
	
	public boolean notifyAboutConfirmation()
	{
		return false;
	}
}
