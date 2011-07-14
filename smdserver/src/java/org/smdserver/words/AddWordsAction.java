package org.smdserver.words;

import com.ccg.util.JavaString;
import java.text.ParseException;
import org.smdserver.actionssystem.SessionKeys;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.smdserver.actionssystem.ActionParams;
import java.util.List;
import java.util.ArrayList;

public class AddWordsAction extends AddWordsBase
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
}