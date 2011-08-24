package org.smdserver.auth;

public interface IAuthLocale
{
	public String getConfirmBody();
	public String getConfirmSubject();
	public String getNotifyAdminAboutRegistrationRequestSubject();
	public String getNotifyAdminAboutRegistrationRequestBody();
	public String getSubjectPrefix();
}
