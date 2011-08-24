package org.smdserver.auth;

import javax.servlet.http.HttpServletRequest;
import org.smdserver.core.ISmdServletContext;
import org.smdserver.users.UserEx;

public interface UIConfirmationActivity 
{
	public boolean process (IRegisterConfig config,
			               ISmdServletContext context,
						   HttpServletRequest request,
			               UserEx user);
	public String getForwardParam(UserEx user);
}
