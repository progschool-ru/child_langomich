package org.smdserver.auth;

import java.util.HashMap;
import java.util.Map;
import org.smdserver.core.ISmdServletContext;
import org.smdserver.jsp.SmdUrl;

class UNoneConfirmationActivity implements UIConfirmationActivity
{
	public String process (IRegisterConfig config,
			               ISmdServletContext context,
			               String userId, String login, 
						   String password,
						   String email, String about)
	{
			Map<String, Object> params = new HashMap<String, Object>();
			SmdUrl confirmUrl = new SmdUrl("action",
										 "confirmRegistration",
										 null, "", params);		
			params.put("userId", userId);
			return confirmUrl.getURL();
	}
}
