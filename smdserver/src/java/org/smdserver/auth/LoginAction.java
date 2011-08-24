package org.smdserver.auth;

import org.smdserver.actionssystem.SessionKeys;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionException;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.actionssystem.ParamsValidator;
import org.smdserver.core.actions.SmdAction;
import org.smdserver.users.IUsersStorage;
import org.smdserver.users.User;

public class LoginAction extends SmdAction
{
	String login;
	String password;
	
	protected String doAction (HttpServletRequest request)
	{
		IUsersStorage storage = getServletContext().getUsersStorage();

		boolean success = storage.checkPassword(login, password);

		setAnswerParam(ActionParams.SUCCESS, success);

		if(success)
		{
			User user = storage.getUserByLogin(login);
			setAnswerParam(ActionParams.USER, user);
			makeUserLoggedIn(user, request);
			return null;
		}
		else
		{
			makeUserLoggedOut(request);
			return null;
		}
	}
	
	@Override
	protected boolean validateParams(HttpServletRequest request) throws ActionException
	{
		ParamsValidator v = new ParamsValidator(request);
		
		login = v.getNotEmpty(ActionParams.LOGIN);
		password = v.getNotEmpty(ActionParams.PASSWORD);
		
		return true;
	}
	
	public static void makeUserLoggedIn(User user, HttpServletRequest request)
	{
		request.getSession().setAttribute(SessionKeys.CURRENT_USER_ID, user.getUserId());
		request.getSession().setAttribute(SessionKeys.CURRENT_LOGIN, user.getLogin());
	}
	
	public static void makeUserLoggedOut(HttpServletRequest request)
	{
		request.getSession().setAttribute(SessionKeys.CURRENT_USER_ID, null);
		request.getSession().setAttribute(SessionKeys.CURRENT_LOGIN, null);
	}
}
