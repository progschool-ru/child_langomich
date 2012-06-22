package org.smdserver.auth;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionParams;
//import org.smdserver.jsp.SmdUrl;
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
		return sendNotifiactionAboutConfirmation(user, user.getEmail(),
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
//		subject = constructSubject(subject, user.getUserId());
//		
//		Map<String, Object> confirmParams = new HashMap<String,Object>();
//		confirmParams.put(ActionParams.KEY, Messages.REGISTER_ACCOUNT_WAS_CREATED);
//		SmdUrl confirmRedirect = new SmdUrl("page", "message", null, confirmParams);
//		
//		Map<String, Object> refuseParams = new HashMap<String,Object>();
//		refuseParams.put(ActionParams.KEY, Messages.REGISTER_REQUEST_REFUSED);
//		SmdUrl refuseRedirect = new SmdUrl("page", "message", null, refuseParams);		
//
//		
//		Map<String, Object> mainConfirmParams = new HashMap<String, Object>();
//		mainConfirmParams.put(ActionParams.USER_ID, user.getUserId());
//		mainConfirmParams.put(ActionParams.REDIRECT_SUCCESS, confirmRedirect.getURL());
//		
//		Map<String, Object> mainRefuseParams = new HashMap<String, Object>();
//		mainRefuseParams.put(ActionParams.USER_ID, user.getUserId());
//		mainRefuseParams.put(ActionParams.REDIRECT_SUCCESS, refuseRedirect.getURL());
//
//		SmdUrl confirmUrl = new SmdUrl("action", "confirmRegistration", null, mainConfirmParams);
//		String confirmLink = serverString + confirmUrl.getURL();
//		
//		SmdUrl refuseUrl = new SmdUrl("action", "refuseRegistration", null, mainRefuseParams);
//		String refuseLink = serverString + refuseUrl.getURL();
//		
//		String message = String.format(template, user.getLogin(), 
//				                                 confirmLink, refuseLink,
//												 user.getUserId(),
//												 user.getEmail(),
//												 user.getAbout());
//		
//		return mailman.send(subject, message, to);
		return true;
	}
	
	private boolean sendNotifiactionAboutConfirmation(UserEx user, String to,
			                                String template, String subject)
	{
//		subject = constructSubject(subject, user.getUserId());
//		
//		SmdUrl loginUrl = new SmdUrl("page", "login");
//		String loginLink = serverString + loginUrl.getURL();
//		
//		String message = String.format(template, user.getLogin(),
//												 user.getUserId(),
//												 user.getEmail(),
//												 user.getAbout(),
//												 loginLink);
//		return mailman.send(subject, message, to);
		return true;
	}
	
	private String constructSubject(String subject, String userId)
	{
		return locale.getSubjectPrefix() + " " + String.format(subject, userId);
	}
}
