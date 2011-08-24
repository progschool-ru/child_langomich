package org.smdserver.auth;

import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionException;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.actionssystem.ParamsValidator;
import org.smdserver.core.ISmdServletContext;
import org.smdserver.core.actions.SmdAction;
import org.smdserver.mail.IMailman;
import org.smdserver.users.IUsersStorage;
import org.smdserver.users.User;
import org.smdserver.users.UserEx;
import org.smdserver.core.small.SmdException;

public class ConfirmRegistrationAction extends SmdAction
{
	private String userId;
	
	protected String doAction (HttpServletRequest request) throws SmdException
	{
		ISmdServletContext cntxt = getServletContext();
		IUsersStorage storage = cntxt.getUsersStorage();
		
		boolean success = storage.confirmRegistration(userId);
		
		if(success)
		{			
			UserEx userEx = storage.getUserExById(userId);
			User user = userEx.generateUser();
			setAnswerParam(ActionParams.USER, user);
			LoginAction.makeUserLoggedIn(user, request);
			
			
			IMailman mailman = cntxt.getMailman();
			IRegisterConfig config = cntxt.getFactory().createRegisterConfig();
			IAuthLocale locale = cntxt.getFactory().createAuthLocale(request.getLocale());
			URegistrationMailer mailer = new URegistrationMailer(mailman, config, 
					                                             request, locale);	
			boolean wasNotified = mailer.notifyAboutConfirmation();
			if(!wasNotified)
			{
				setAnswerMessage("Notification wasn't sent");
			}
		}
		setAnswerSuccess(success);	
		return null;
	}
	
	@Override
	protected boolean validateParams(HttpServletRequest request) throws ActionException
	{
		ParamsValidator v = new ParamsValidator(request);
		
		userId = v.getNotEmpty(ActionParams.USER_ID);
		
		return true;
	}	
}
