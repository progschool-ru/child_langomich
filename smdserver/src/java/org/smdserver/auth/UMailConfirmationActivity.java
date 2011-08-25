package org.smdserver.auth;

import javax.servlet.http.HttpServletRequest;
import org.smdserver.core.ISmdServletContext;
import org.smdserver.users.UserEx;

class UMailConfirmationActivity implements UIConfirmationActivity
{
	public AnswerKey process (IRegisterConfig config,
			               ISmdServletContext context,
						   HttpServletRequest request,
			               UserEx user)
	{
		IAuthLocale locale = context.getFactory().createAuthLocale(request.getLocale());
		URegistrationMailer mailer = new URegistrationMailer(context.getMailman(), 
															 config, request,
				                                             locale);
		
		if(config.shouldNotifyAdmin())
		{
			mailer.notifyAdminAboutRegistrationRequest(user);
		}
		
		if(mailer.sendConfirmationMessage(user))
		{
			return AnswerKey.REGISTER_CONFIRM_SENT;
		}
		else
		{
			return AnswerKey.REGISTER_NOTIFICATION_WASNT_CREATED;
		}
	}
	
	public String getForwardParam(UserEx user)
	{
		return null;
	}
}
