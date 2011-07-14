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

public class LoginAction extends SmdAction
{
	protected String doAction (HttpServletRequest request)
	{
                if(request.getSession().getAttribute(SessionKeys.CURRENT_LOGIN)!= null) {
				//TODO: 3.low. Посте того, как разберёмся со всеми jsp страницами,
				//эту логику надо будет уобрать.
				//Кажется более логичным, разлогиниваться
				//в случае, если послан запрос на логин с нправильным паролем.
                    return "/profile.jsp";
                }
		String login = request.getParameter(ActionParams.LOGIN);
		String password = request.getParameter(ActionParams.PASSWORD);
		IUsersStorage storage = getServletContext().getUsersStorage();

		boolean success = storage.checkPassword(login, password);

                setAnswerParam(ActionParams.SUCCESS, success);

		if(success)
		{
			setAnswerParam(ActionParams.USER, storage.getUserByLogin(login));
                        request.getSession().setAttribute(SessionKeys.CURRENT_LOGIN, login);

                        IWordsStorage WStorage = getServletContext().getWordsStorage();
                        List<Language> languages = WStorage.getUserWords(storage.getUserByLogin(login).getUserId());
                        ArrayList al = new ArrayList();
                        for(int i = 0; i < languages.size();i++)
                            al.add(languages.get(i));
                        request.getSession().setAttribute(SessionKeys.LANGUAGES, al);
                        //return "/main.jsp";
						return null;
                }
		else
		{
			request.getSession().setAttribute(SessionKeys.CURRENT_LOGIN, null);
                        request.getSession().setAttribute(SessionKeys.LANGUAGES, null);
                        //return "/login.jsp";
						return null;
                }
	}
}
