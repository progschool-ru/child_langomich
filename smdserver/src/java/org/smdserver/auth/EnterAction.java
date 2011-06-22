package org.smdserver.auth;

import javax.servlet.http.HttpServletRequest;
import org.smdserver.core.SmdAction;

public class EnterAction extends SmdAction
{
	protected String doAction (HttpServletRequest request)
	{
            return "/main.jsp";
	}
}