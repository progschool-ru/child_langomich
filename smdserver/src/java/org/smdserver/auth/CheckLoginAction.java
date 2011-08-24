package org.smdserver.auth;

import javax.servlet.http.HttpServletRequest;
import org.smdserver.users.User;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.actionssystem.SessionKeys;
import org.smdserver.core.actions.SmdAction;

public abstract class CheckLoginAction extends SmdAction
{
	private User user;
	
	@Override
	protected boolean validateContext (HttpServletRequest request)
	{
		String userId = (String)request.getSession().getAttribute(SessionKeys.CURRENT_USER_ID);
		if(userId != null)
		{
			user = getServletContext().getUsersStorage().getUserById(userId);
		}
		boolean success = user != null;
		setAnswerParam(ActionParams.SUCCESS, success);
		return success;
	}

	protected String getLogin ()
	{
		return user.getLogin();
	}

	protected User getUser ()
	{
		return user;
	}
}
