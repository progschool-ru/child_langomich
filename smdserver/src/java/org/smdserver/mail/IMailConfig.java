package org.smdserver.mail;

public interface IMailConfig 
{
	public String getSMTPHost();
	public boolean isAuth();
	public String getLogin();
	public String getPassword();
	public String getFrom();
	public boolean isEnabled();
}
