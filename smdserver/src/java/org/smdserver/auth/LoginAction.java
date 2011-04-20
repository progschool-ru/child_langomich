package org.smdserver.auth;

import org.smdserver.actionssystem.SessionKeys;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.actionssystem.Action;
import org.smdserver.users.UsersStorage;

public class LoginAction extends Action
{
	protected String doAction(HttpServletRequest request)
	{
		String login = request.getParameter(ActionParams.LOGIN);
		String password = request.getParameter(ActionParams.PASSWORD).toString();
		UsersStorage storage = getServletContext().getUsersStorage();

		boolean success = storage.checkPassword(login, password);

		if(success)
		{
			setAnswerParam(ActionParams.USER, storage.getUserByLogin(login));
			request.getSession().setAttribute(SessionKeys.CURRENT_LOGIN, login);
		}
		else
		{
			request.getSession().setAttribute(SessionKeys.CURRENT_LOGIN, null);
		}

		setAnswerParam(ActionParams.SUCCESS, new Boolean(success));
		return null;
	}
}
