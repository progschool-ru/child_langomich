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

		if(success)
		{
			try
			{
				getServletContext().getUsersStorage().setPassword(login, password);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				success = false;
			}
		}

		setAnswerParam(ActionParams.SUCCESS, new Boolean(success));
		return null;
	}
}