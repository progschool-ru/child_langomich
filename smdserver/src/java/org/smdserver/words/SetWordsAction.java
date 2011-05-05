package org.smdserver.words;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.auth.CheckLoginAction;

public class SetWordsAction extends CheckLoginAction
{	
	protected String doAction(HttpServletRequest request)
	{
		String dataString = request.getParameter(ActionParams.DATA);

		try
		{
			JSONObject json = new JSONObject(dataString);
			List<Language> languages = parseJSON(json.getJSONArray(ActionParams.LANGUAGES));
			IWordsStorage storage = getServletContext().getWordsStorage();
			storage.setUserWords(getUser().getUserId(), languages);
			setAnswerParam(ActionParams.SUCCESS, true);
		}
		catch(JSONException e)
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

	private List<Language> parseJSON(JSONArray json) throws JSONException, ParseException
	{
		List<Language> languages = new ArrayList<Language>();
		int length = json.length();

		for(int i = 0; i < length; i++)
		{
            JSONObject value = json.getJSONObject(i);
			languages.add(new Language(value));
		}

		return languages;
	}

	@Override
	protected boolean validateParams(HttpServletRequest request)
	{
		return request.getParameter(ActionParams.DATA) != null;
	}
}
