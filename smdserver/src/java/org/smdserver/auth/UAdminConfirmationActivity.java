package org.smdserver.auth;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.core.ISmdServletContext;
import org.smdserver.users.UserEx;

class UAdminConfirmationActivity implements UIConfirmationActivity
{
	public AnswerKey process (IRegisterConfig config,
			               ISmdServletContext context,
						   HttpServletRequest request,
			               UserEx user)
	{
		IAuthLocale locale = context.getFactory().createAuthLocale(Locale.getDefault());
		URegistrationMailer mailer = new URegistrationMailer(context.getMailman(), 
															 config, request,
				                                             locale);
		
		if(mailer.notifyAdminAboutRegistrationRequest(user))
		{
			return AnswerKey.REGISTER_CONFIRM_SENT_TO_ADMIN;
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
