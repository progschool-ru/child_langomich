package org.smdserver.words;

import com.ccg.util.JavaString;
import java.text.ParseException;
import org.smdserver.actionssystem.SessionKeys;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.auth.CheckLoginAction;
import java.util.List;
import java.util.ArrayList;

public class AddWordsAction extends CheckLoginAction
{	
	protected String doAction (HttpServletRequest request)
	{
		String dataString = request.getParameter(ActionParams.DATA);
                
		try
		{ 
			JSONObject json = new JSONObject(JavaString.decode(dataString));
			List<Language> languages = parseJSON(json.getJSONArray(ActionParams.LANGUAGES));
			IWordsStorage storage = getServletContext().getWordsStorage();
			storage.addUserWords(getUser().getUserId(), languages);
			setAnswerParam(ActionParams.SUCCESS, true);


			languages = storage.getUserWords(getUser().getUserId());
			ArrayList al = new ArrayList();
			for(int i = 0; i < languages.size();i++)
				al.add(languages.get(i));
			request.getSession().setAttribute(SessionKeys.LANGUAGES, al);
			return "/main.jsp";
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
		return "/addWords.jsp";
	}

	private List<Language> parseJSON (JSONArray json) throws WordsException
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


