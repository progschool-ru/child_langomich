package org.smdserver.words;

import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.smdserver.actionssystem.ActionParams;
import java.util.List;
import java.util.ArrayList;

public class AddWordsMobileAction extends AddWordsBase
{	
	protected String doAction (HttpServletRequest request)
	{
		String dataString = request.getParameter(ActionParams.DATA);

		try
		{
			JSONObject json = new JSONObject(dataString);
			long lastModified = json.getLong("lastModified");
			int numberOfTiming = json.getInt("numberOfTiming");

			IWordsStorage storage = getServletContext().getWordsStorage();
			List<Language> languagesOut;
			if(numberOfTiming == 0)
				languagesOut = storage.getUserWords(getUser().getUserId());
			else
				languagesOut = storage.getUserWords(getUser().getUserId(), lastModified);

			setAnswerParam(ActionParams.LANGUAGES, languagesOut);

			List<Language> languagesIn = new ArrayList(parseJSON(json.getJSONArray(ActionParams.LANGUAGES)));
			storage.addUserWords(getUser().getUserId(), languagesIn);
			setAnswerParam(ActionParams.SUCCESS, true);

//			languagesIn = storage.getUserWords(getUser().getUserId());
//			ArrayList al = new ArrayList();
//			for(int i = 0; i < languagesIn.size();i++)
//				al.add(languagesIn.get(i));
//			request.getSession().setAttribute(SessionKeys.LANGUAGES, al);
			return null;
		}
		catch(JSONException e)
		{
			setAnswerParam(ActionParams.SUCCESS, false);
			setAnswerParam(ActionParams.MESSAGE, e.getMessage());
		}
		catch(WordsException e)
		{
			setAnswerParam(ActionParams.SUCCESS, false);
			setAnswerParam(ActionParams.MESSAGE, e.getMessage());
		}
		return null;
	}
}
