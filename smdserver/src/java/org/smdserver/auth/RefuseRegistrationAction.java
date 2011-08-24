package org.smdserver.auth;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionException;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.actionssystem.ParamsValidator;
import org.smdserver.core.ISmdServletContext;
import org.smdserver.core.actions.SmdAction;
import org.smdserver.core.small.SmdException;
import org.smdserver.mail.IMailman;
import org.smdserver.users.IUsersStorage;

public class RefuseRegistrationAction extends SmdAction
{
	private String userId;
	
	protected String doAction (HttpServletRequest request) throws SmdException
	{
		ISmdServletContext cntxt = getServletContext();
		IUsersStorage storage = cntxt.getUsersStorage();
		
		boolean success = storage.removeRegistratioinRequestById(userId);
		
		if(success)
		{
			IRegisterConfig config = cntxt.getFactory().createRegisterConfig();
			if(config.shouldNotifyAdmin())
			{
				IMailman mailman = cntxt.getMailman();
				IAuthLocale locale = cntxt.getFactory().createAuthLocale(Locale.getDefault());
				URegistrationMailer mailer = new URegistrationMailer(mailman, config, 
					                                             request, locale);
				mailer.notifyAdminAboutRefusing(userId);
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
