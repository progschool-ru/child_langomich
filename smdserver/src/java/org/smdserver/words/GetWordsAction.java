package org.smdserver.words;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.auth.CheckLoginAction;

public class GetWordsAction extends CheckLoginAction
{
	protected String doAction (HttpServletRequest request)
	{
		IWordsStorage storage = getServletContext().getWordsStorage();
		List<Language> languages = storage.getUserWords(getUser().getUserId());
		setAnswerParam(ActionParams.LANGUAGES, languages);
		setAnswerParam(ActionParams.SUCCESS, true);
                return null;
	}
}
