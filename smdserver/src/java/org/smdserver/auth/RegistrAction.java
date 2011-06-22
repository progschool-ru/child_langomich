package org.smdserver.auth;

import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.core.SmdAction;
import org.smdserver.users.IUsersStorage;

public class RegistrAction extends SmdAction
{
	protected String doAction (HttpServletRequest request)
	{
		String login = request.getParameter(ActionParams.LOGIN);
		String password = request.getParameter(ActionParams.PASSWORD);
		IUsersStorage storage = getServletContext().getUsersStorage();

		boolean success = (login != null && password != null && !password.isEmpty() && storage.checkLogin(login));

                if(success)
                        try
			{
                                getServletContext().getUsersStorage().createUser( storage.getID(),login, password);
                                setAnswerParam(ActionParams.SUCCESS, success);
                                return "/action/login?login="+login+"&password="+password;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				success = false;
			}
                setAnswerParam(ActionParams.SUCCESS, success);
                return "/registr.jsp";
        }
}