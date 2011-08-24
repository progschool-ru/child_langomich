package org.smdserver.words;

import com.ccg.util.JavaString;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.auth.CheckLoginAction;

public class DeleteWordsAction extends CheckLoginAction
{
	protected String doAction (HttpServletRequest request)
	{
		boolean success = false;
		try
		{
			String languageId = request.getParameter(ActionParams.LANGUAGE_ID);
			String wordsParam = request.getParameter(ActionParams.WORDS);

			JSONArray wordsJSON = new JSONArray(JavaString.decode(wordsParam));
			
			Language language = new Language(languageId, null);
			for(int i = 0; i < wordsJSON.length(); i++)
			{
				String original = wordsJSON.getString(i);
				language.getWords().add(new Word(original, "", 0));
			}
			
			List<Language> list = new ArrayList<Language>();
			list.add(language);
			
			IWordsStorage st = getServletContext().getWordsStorage();
			String userId = getUser().getUserId();

			success = st.addUserWords(userId, list, System.currentTimeMillis());
		}
		catch(Exception e)
		{
			log(e);
			success = false;
		}
		setAnswerSuccess(success);
		return null;
	}
}
