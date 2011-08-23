package org.smdserver.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionException;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.actionssystem.ParamsValidator;
import org.smdserver.core.SmdAction;
import org.smdserver.util.SmdException;
import org.smdserver.jsp.SmdUrl;
import org.smdserver.users.IUsersStorage;

public class RegistrAction extends SmdAction
{
	private String login;
	private String password;
	private String email;
	private String about;
	
	protected String doAction (HttpServletRequest request) throws SmdException
	{
		IUsersStorage storage = getServletContext().getUsersStorage();

		boolean success;
		String result = null;

		success = (login != null && 
				   password != null && 
				   !password.isEmpty() && 
				   !storage.doesLoginExist(login));

		if(success)
		{
			//TODO: (3.low)[#26069] create and use universal ID generator
			String uuid = UUID.randomUUID().toString();
			storage.createRegistrationRequest(uuid, login, password, email, about);
				
//			setAnswerParam(ActionParams.SUCCESS, success);
//
//			Map<String, Object> params = new HashMap<String, Object>();
//			SmdUrl loginUrl = new SmdUrl("action",
//										 "login?" + getRedirectParamsURI(request),
//										 null, "", params);		
//			params.put("password", password);
//			params.put("login", login);
//			System.out.println("succ " + success);
//			return loginUrl.getURL();				
		}
		
		setAnswerParam(ActionParams.SUCCESS, success);		
		return result;
	}
	
	@Override
	protected boolean validateParams(HttpServletRequest request) throws ActionException
	{
		ParamsValidator v = new ParamsValidator(request);
		
		login = v.getNotEmpty(ActionParams.LOGIN);
		password = v.getNotEmpty(ActionParams.PASSWORD);
		email = v.getEmail(ActionParams.EMAIL);
		about = request.getParameter(ActionParams.ABOUT);
		
		return true;
	}
}