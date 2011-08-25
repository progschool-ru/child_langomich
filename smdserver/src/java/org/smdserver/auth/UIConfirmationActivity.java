package org.smdserver.auth;

import javax.servlet.http.HttpServletRequest;
import org.smdserver.core.ISmdServletContext;
import org.smdserver.users.UserEx;

interface UIConfirmationActivity 
{
	public AnswerKey process (IRegisterConfig config,
			               ISmdServletContext context,
						   HttpServletRequest request,
			               UserEx user);
	public String getForwardParam(UserEx user);
}
