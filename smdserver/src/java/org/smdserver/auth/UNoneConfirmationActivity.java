package org.smdserver.auth;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.core.ISmdServletContext;
import org.smdserver.jsp.SmdUrl;
import org.smdserver.users.UserEx;

class UNoneConfirmationActivity implements UIConfirmationActivity
{
	public AnswerKey process (IRegisterConfig config,
			               ISmdServletContext context,
						   HttpServletRequest request,
			               UserEx user)
	{
		return AnswerKey.NULL_KEY;
	}
	
	public String getForwardParam(UserEx user)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		SmdUrl confirmUrl = new SmdUrl("action", "confirmRegistration",
									 null, params);
		params.put("userId", user.getUserId());
		return confirmUrl.getForwardPath();
	}
}
