package org.smdserver.auth;

import org.smdserver.core.ISmdServletContext;

public interface UIConfirmationActivity 
{
	public String process (IRegisterConfig config,
			               ISmdServletContext context,
			               String userId, String login, String password,
						   String email, String about);
}
