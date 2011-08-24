package org.smdserver.auth;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionException;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.actionssystem.ParamsValidator;
import org.smdserver.core.ISmdCoreFactory;
import org.smdserver.core.actions.SmdAction;
import org.smdserver.core.small.SmdException;
import org.smdserver.users.IUsersStorage;

public class RegisterAction extends SmdAction
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
		String message = null;

		if(!storage.doesLoginExist(login))
		{
			//TODO: (3.low)[#26069] create and use universal ID generator
			String uuid = UUID.randomUUID().toString();
			boolean created = storage.createRegistrationRequest(uuid, login, password, email, about);
			
			if(created)
			{
				ISmdCoreFactory factory = getServletContext().getFactory();
				IRegisterConfig config = factory.createRegisterConfig();
				ConfirmationType type = config.getConfirmationType();
				
				result = type.getActivity().process(config,
						                            getServletContext(), 
						                            uuid, login, password, 
						                            email, about);
				
				success = true;
			}
			else
			{
				success = false;
				setAnswerMessage("Registration request wasn't created"); //TODO: (3.low) Move to locale;
			}							
		}
		else
		{
			success = false;
			setAnswerMessage("Login already exists"); //TODO: (3.low) Move to locale;
		}
		
		setAnswerSuccess(success);
		
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