package org.smdserver.auth;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.core.ISmdServletContext;
import org.smdserver.users.UserEx;

public class UAdminConfirmationActivity implements UIConfirmationActivity
{
	public boolean process (IRegisterConfig config,
			               ISmdServletContext context,
						   HttpServletRequest request,
			               UserEx user)
	{
		IAuthLocale locale = context.getFactory().createAuthLocale(Locale.getDefault());
		URegistrationMailer mailer = new URegistrationMailer(context.getMailman(), 
															 config, request,
				                                             locale);
		
		return mailer.notifyAdminAboutRegistrationRequest(user);
	}
	
	public String getForwardParam(UserEx user)
	{
		return null;
	}	
}
