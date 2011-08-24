package org.smdserver.auth;

import javax.servlet.http.HttpServletRequest;
import org.smdserver.users.User;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.actionssystem.SessionKeys;
import org.smdserver.core.actions.SmdAction;

public abstract class CheckLoginAction extends SmdAction
{
	private String login;
	
	@Override
	protected boolean validateContext (HttpServletRequest request)
	{
		login = (String)request.getSession().getAttribute(SessionKeys.CURRENT_LOGIN);
		boolean success = login != null;
		setAnswerParam(ActionParams.SUCCESS, success);
		return success;
	}

	protected String getLogin ()
	{
		return login;
	}

	protected User getUser ()
	{
		return getServletContext().getUsersStorage().getUserByLogin(login);
	}
}
