package org.smdserver.words;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.auth.CheckLoginAction;

public class GetLanguagesAction extends CheckLoginAction
{
	protected String doAction (HttpServletRequest request)
	{
		IWordsStorage storage = getServletContext().getWordsStorage();
		List<Language> languages = storage.getLanguages(getUser().getUserId());
		setAnswerParam(ActionParams.LANGUAGES, languages);
		setAnswerSuccess(true);
		return null;
	}	
}
