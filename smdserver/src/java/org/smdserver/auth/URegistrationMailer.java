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
	
	public boolean notifyAboutConfirmation()
	{
		return false;
	}
	
	public boolean sendConfirmationMessage(UserEx user)
	{
		return sendConfirmationMessage(user, user.getEmail(),
				           locale.getConfirmBody(), locale.getConfirmSubject());
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
		subject = locale.getSubjectPrefix() + " " + subject;
		
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
}
