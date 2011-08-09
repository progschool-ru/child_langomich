package org.smdserver.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.core.SmdAction;
import org.smdserver.core.SmdException;
import org.smdserver.jsp.SmdUrl;
import org.smdserver.users.IUsersStorage;

public class RegistrAction extends SmdAction
{
	protected String doAction (HttpServletRequest request)
	{
		String login = request.getParameter(ActionParams.LOGIN);
		String password = request.getParameter(ActionParams.PASSWORD);
		IUsersStorage storage = getServletContext().getUsersStorage();

		boolean success;
		try
		{
			success = (login != null && password != null && !password.isEmpty() && !storage.doesLoginExist(login));
		}
		catch(SmdException e)
		{
			success = false;
		}

		if(success)
		{
			try
			{
				//TODO: (3.low)[#26069] create and use universal ID generator
				String uuid = UUID.randomUUID().toString();
				getServletContext().getUsersStorage().createUser( uuid, login, password);
				setAnswerParam(ActionParams.SUCCESS, success);

				Map<String, Object> params = new HashMap<String, Object>();
				SmdUrl loginUrl = new SmdUrl("action",
						                     "login?" + getRedirectParamsURI(request),
											 null, "", params);		
				params.put("password", password);
				params.put("login", login);
				return loginUrl.getURL();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				success = false;
			}
		}
		setAnswerParam(ActionParams.SUCCESS, success);
		return null;
	}
}