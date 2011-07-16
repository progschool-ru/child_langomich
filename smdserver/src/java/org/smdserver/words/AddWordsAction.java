package org.smdserver.words;

import com.ccg.util.JavaString;
import java.text.ParseException;
import org.smdserver.actionssystem.SessionKeys;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.smdserver.actionssystem.ActionParams;
import java.util.List;
import java.util.ArrayList;
import org.smdserver.auth.CheckLoginAction;

public class AddWordsAction extends CheckLoginAction
{	
	protected String doAction (HttpServletRequest request)
	{
		String dataString = request.getParameter(ActionParams.DATA);
                
		try
		{ 
			JSONObject json = new JSONObject(JavaString.decode(dataString));
			long lastModified = 0;
			int numberOfTiming = 0;
			try
			{
				lastModified = json.getLong("lastModified");
				numberOfTiming = json.getInt("numberOfTiming");
			}
			catch(JSONException e){}
			IWordsStorage storage = getServletContext().getWordsStorage();
			if(lastModified != 0)
			{
				List<Language> languages;
				if(numberOfTiming == 0)
					languages = storage.getCopyUserWords(getUser().getUserId());
				else
					languages = storage.getCopyUserWords(getUser().getUserId(), lastModified);

				setAnswerParam(ActionParams.LANGUAGES, languages);
				setAnswerParam("mobile", "true");
			}

			List<Language> languages = parseJSON(json.getJSONArray(ActionParams.LANGUAGES));
			storage.addUserWords(getUser().getUserId(), languages);
			setAnswerParam(ActionParams.SUCCESS, true);

			languages = storage.getUserWords(getUser().getUserId());
			ArrayList al = new ArrayList();
			for(int i = 0; i < languages.size();i++)
				al.add(languages.get(i));
			request.getSession().setAttribute(SessionKeys.LANGUAGES, al);
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
		catch(ParseException e)
		{
				setAnswerParam(ActionParams.SUCCESS, false);
				setAnswerParam(ActionParams.MESSAGE, e.getMessage());
		}
		return null;
	}
	protected List<Language> parseJSON (JSONArray json) throws WordsException
	{
		List<Language> languages = new ArrayList<Language>();
		int length = json.length();

		try
		{
			for(int i = 0; i < length; i++)
			{
				JSONObject value = json.getJSONObject(i);
				languages.add(new Language(value));
			}
		}
		catch(JSONException e)
		{
			throw new WordsException(WordsException.JSON_ERROR + "; " + e.getMessage());
		}

		return languages;
	}

	@Override
	protected boolean validateParams (HttpServletRequest request)
	{
		return request.getParameter(ActionParams.DATA) != null;
	}
}