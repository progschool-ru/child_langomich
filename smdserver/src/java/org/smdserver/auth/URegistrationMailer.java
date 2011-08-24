package org.smdserver.auth;

import org.smdserver.mail.IMailman;

class URegistrationMailer 
{
	IRegisterConfig config;
	IMailman mailman;
	
	public URegistrationMailer(IMailman mailman, IRegisterConfig config)
	{
		this.config = config;
		this.mailman = mailman;
	}
	
	public boolean notifyAboutConfirmation()
	{
		return false;
	}
}
