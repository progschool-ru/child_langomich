package org.smdserver.auth;

import org.smdserver.actionssystem.SessionKeys;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.core.SmdAction;
import org.smdserver.users.IUsersStorage;

public class MobileLoginAction extends SmdAction
{
	protected String doAction (HttpServletRequest request)
	{
		String login = request.getParameter(ActionParams.LOGIN);
		String password = request.getParameter(ActionParams.PASSWORD);
		IUsersStorage storage = getServletContext().getUsersStorage();

		boolean success = storage.checkPassword(login, password);

                setAnswerParam(ActionParams.SUCCESS, success);

		if(success)
		{
			setAnswerParam(ActionParams.USER, storage.getUserByLogin(login));
                        request.getSession().setAttribute(SessionKeys.CURRENT_LOGIN, login);                       
                }
		else
		{
			request.getSession().setAttribute(SessionKeys.CURRENT_LOGIN, null);
                }
                return null;
	}
}
