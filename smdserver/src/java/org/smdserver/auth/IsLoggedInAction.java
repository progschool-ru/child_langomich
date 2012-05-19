
package org.smdserver.auth;

import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.actionssystem.SessionKeys;
import org.smdserver.core.actions.SmdAction;
import org.smdserver.users.User;

public class IsLoggedInAction extends SmdAction
{
	protected String doAction (HttpServletRequest request)
	{
		String userId = (String)request.getSession().getAttribute(SessionKeys.CURRENT_USER_ID);
		User user = null;
		if(userId != null)
		{
			user = getServletContext().getUsersStorage().getUserById(userId);
		}
		boolean isLoggedIn = user != null;

		setAnswerSuccess(true);
		setAnswerParam(ActionParams.IS_LOGGED_IN, isLoggedIn);
		return null;
	}
}
