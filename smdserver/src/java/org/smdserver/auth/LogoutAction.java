package org.smdserver.auth;

import org.smdserver.actionssystem.SessionKeys;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.core.SmdAction;

public class LogoutAction extends SmdAction
{
	protected String doAction (HttpServletRequest request)
	{
            request.getSession().setAttribute(SessionKeys.CURRENT_LOGIN, null);
            request.getSession().setAttribute(SessionKeys.LANGUAGES, null);
            return "/main.jsp";
	}
}