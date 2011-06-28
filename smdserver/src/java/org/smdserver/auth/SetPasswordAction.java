package org.smdserver.auth;

import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionParams;

public class SetPasswordAction extends CheckLoginAction
{
	protected String doAction (HttpServletRequest request)
	{
		String login = getLogin();
		String password = request.getParameter(ActionParams.PASSWORD);

		boolean success = (login != null && password != null && !password.isEmpty());
                setAnswerParam(ActionParams.SUCCESS, success);
		if(success)
		{
			try
			{
				getServletContext().getUsersStorage().setPassword(login, password);
                                return "/profile.jsp";
                        }
			catch(Exception e)
			{
				e.printStackTrace();
				success = false;

			}
		}
                setAnswerParam(ActionParams.SUCCESS, success);
		return "/setPassword.jsp";
	}
}