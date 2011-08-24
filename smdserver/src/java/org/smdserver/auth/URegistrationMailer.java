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
	
	public URegistrationMailer(IMailman mailman, 
			                   IRegisterConfig config,
							   HttpServletRequest request)
	{
		this.config = config;
		this.mailman = mailman;
		
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
		String subject = "Lang Omich: " + "Confirm registration";
		String template = "Hi, %1$s!\nYou've registered account at http://lang.omich.net\n"
				+ "Follow link to confirm your registration: %2$s\n"
				+ "------\nTo refuse your registration follow link: %3$s\n";
		
		Map<String, Object> params = new HashMap<String, Object>();
		SmdUrl url = new SmdUrl("action", "confirmRegistration", null, params);
		SmdUrl mainUrl = new SmdUrl("page", "");
		params.put(ActionParams.USER_ID, user.getUserId());
		params.put(ActionParams.REDIRECT_SUCCESS, mainUrl.getURL());
		String link = serverString + url.getURL();
		
		String message = String.format(template, user.getLogin(), link, "");
		
		return mailman.send(subject, message, user.getEmail());
	}
}
