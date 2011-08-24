package org.smdserver.auth;

import javax.servlet.http.HttpServletRequest;
import org.smdserver.core.ISmdServletContext;
import org.smdserver.users.UserEx;

public class UMailConfirmationActivity implements UIConfirmationActivity
{
	public boolean process (IRegisterConfig config,
			               ISmdServletContext context,
						   HttpServletRequest request,
			               UserEx user)
	{
		URegistrationMailer mailer = new URegistrationMailer(context.getMailman(), 
															 config, request);
		return mailer.sendConfirmationMessage(user);
	}
	
	public String getForwardParam(UserEx user)
	{
		return null;
	}
}
