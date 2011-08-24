package org.smdserver.auth;

import javax.servlet.http.HttpServletRequest;
import org.smdserver.core.actions.SmdAction;

public class LogoutAction extends SmdAction
{
	protected String doAction (HttpServletRequest request)
	{
		LoginAction.makeUserLoggedOut(request);
		return null;
	}
}