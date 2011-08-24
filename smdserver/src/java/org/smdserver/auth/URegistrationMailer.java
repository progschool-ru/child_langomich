package org.smdserver.auth;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.jsp.SmdUrl;
import org.smdserver.mail.IMailman;
import org.smdserver.users.UserEx;

class URegistrationMailer 
{
	IRegisterConfig config;
	IMailman mailman;
	String serverString;
	IAuthLocale locale;
	
	public URegistrationMailer(IMailman mailman, 
			                   IRegisterConfig config,
							   HttpServletRequest request,
							   IAuthLocale locale)
	{
		this.config = config;
		this.mailman = mailman;
		this.locale = locale;
		
		String url = request.getRequestURL().toString();
		int first = url.indexOf("//");
		int second = url.indexOf("/", first + 2);
		this.serverString = url.substring(0, second);
	}
	
	public boolean notifyAboutConfirmation(UserEx user)
	{
		return sendNotifiactionAboutConfirmation(user, config.getAdminEmail(),
				        locale.getNotifyUserAboutConfirmationBody(), 
				        locale.getNotifyUserAboutConfirmationSubject());
	}
	
	public boolean sendConfirmationMessage(UserEx user)
	{
		return sendConfirmationMessage(user, user.getEmail(),
				           locale.getConfirmBody(), locale.getConfirmSubject());
	}
	
	public boolean notifyAdminAboutConfirmation(UserEx user)
	{
		return sendNotifiactionAboutConfirmation(user, config.getAdminEmail(),
				        locale.getNotifyAdminAboutConfirmationBody(), 
				        locale.getNotifyAdminAboutConfirmationSubject());
	}
	
	public boolean notifyAdminAboutRefusing (String userId)
	{
		String subject = constructSubject(locale.getNotifyAdminAboutRefusingSubject(), 
				                          userId);
		String template = locale.getNotifyAdminAboutRefusingBody();
		
		String message = String.format(template, userId);
		
		return mailman.send(subject, message, config.getAdminEmail());
	}

	public boolean notifyAdminAboutRegistrationRequest(UserEx user)
	{
		return sendConfirmationMessage(user, config.getAdminEmail(),
				        locale.getNotifyAdminAboutRegistrationRequestBody(), 
				        locale.getNotifyAdminAboutRegistrationRequestSubject());
	}
	
	private boolean sendConfirmationMessage(UserEx user, String to,
			                                String template, String subject)
	{
		subject = constructSubject(subject, user.getUserId());
		
		Map<String, Object> params = new HashMap<String, Object>();
		SmdUrl mainUrl = new SmdUrl("page", "");
		params.put(ActionParams.USER_ID, user.getUserId());
		params.put(ActionParams.REDIRECT_SUCCESS, mainUrl.getURL());
		
		SmdUrl confirmUrl = new SmdUrl("action", "confirmRegistration", null, params);
		String confirmLink = serverString + confirmUrl.getURL();
		
		SmdUrl refuseUrl = new SmdUrl("action", "refuseRegistration", null, params);
		String refuseLink = serverString + refuseUrl.getURL();
		
		String message = String.format(template, user.getLogin(), 
				                                 confirmLink, refuseLink,
												 user.getUserId(),
												 user.getEmail(),
												 user.getAbout());
		
		return mailman.send(subject, message, to);
	}
	
	private boolean sendNotifiactionAboutConfirmation(UserEx user, String to,
			                                String template, String subject)
	{
		subject = constructSubject(subject, user.getUserId());
		
		SmdUrl loginUrl = new SmdUrl("page", "login");
		String loginLink = serverString + loginUrl.getURL();
		
		String message = String.format(template, user.getLogin(),
												 user.getUserId(),
												 user.getEmail(),
												 user.getAbout(),
												 loginLink);
		return mailman.send(subject, message, to);
	}
	
	private String constructSubject(String subject, String userId)
	{
		return locale.getSubjectPrefix() + " " + String.format(subject, userId);
	}
}
