package org.smdserver.auth;

import org.smdserver.actionssystem.SessionKeys;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.core.SmdAction;
import org.smdserver.users.IUsersStorage;
import org.smdserver.words.Language;
import org.smdserver.words.IWordsStorage;
import java.util.List;
import java.util.ArrayList;
import org.smdserver.users.User;

public class LoginAction extends SmdAction
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
			User user = storage.getUserByLogin(login);
			setAnswerParam(ActionParams.USER, user);
			request.getSession().setAttribute(SessionKeys.CURRENT_LOGIN, user.getLogin());

			IWordsStorage WStorage = getServletContext().getWordsStorage();
			List<Language> languages = WStorage.getUserWords(user.getUserId());
			ArrayList al = new ArrayList();
			for(int i = 0; i < languages.size();i++)
				al.add(languages.get(i));
			request.getSession().setAttribute(SessionKeys.LANGUAGES, al);
			return null;
		}
		else
		{
			request.getSession().setAttribute(SessionKeys.CURRENT_LOGIN, null);
			request.getSession().setAttribute(SessionKeys.LANGUAGES, null);
			return null;
		}
	}
}
