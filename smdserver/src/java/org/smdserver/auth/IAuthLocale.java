package org.smdserver.auth;

public interface IAuthLocale
{
	public String getConfirmBody();
	public String getConfirmSubject();
	public String getNotifyAdminAboutConfirmationSubject();
	public String getNotifyAdminAboutConfirmationBody();
	public String getNotifyAdminAboutRefusingSubject();
	public String getNotifyAdminAboutRefusingBody();
	public String getNotifyAdminAboutRegistrationRequestSubject();
	public String getNotifyAdminAboutRegistrationRequestBody();
	public String getNotifyUserAboutConfirmationSubject();
	public String getNotifyUserAboutConfirmationBody();
	public String getSubjectPrefix();
}
