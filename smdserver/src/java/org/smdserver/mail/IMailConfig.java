package org.smdserver.mail;

public interface IMailConfig 
{
	public boolean isAuth();
	public boolean isEnabled();
	public String getFrom();
	public String getLogin();
	public String getPassword();
	public String getReplyTo();
	public String getSMTPHost();
}
