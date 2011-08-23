package org.smdserver.auth;

public interface IRegisterConfig 
{
	public ConfirmationType getConfirmationType();
	public boolean shouldNotifyAdmin();
	public String getAdminEmail();
}
