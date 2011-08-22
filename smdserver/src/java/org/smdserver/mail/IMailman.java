package org.smdserver.mail;

public interface IMailman 
{
	public boolean send(String subject, String text, String to);
}
