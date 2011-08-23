package org.smdserver.words;

import com.ccg.util.JavaString;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.smdserver.actionssystem.ActionParams;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import org.smdserver.auth.CheckLoginAction;
import org.smdserver.util.SmdException;

public class AddWordsAction extends CheckLoginAction
{	
	protected String doAction (HttpServletRequest request)
	{
		String dataString = request.getParameter(ActionParams.DATA);

		try
		{ 
			JSONObject json = new JSONObject(JavaString.decode(dataString));
			
			long lastConnection = json.has(ActionParams.LAST_CONNECTION) 
					              ? json.getLong(ActionParams.LAST_CONNECTION) 
					              : 0;
			long currentConnection = new Date().getTime();

			IWordsStorage storage = getServletContext().getWordsStorage();
			String userId = getUser().getUserId();

			if(lastConnection >= 0)
			{
				// Добываем все обновления прошедшие позднее чем через миллисикунду после предыдущего обновления.
				// Это мы делаем для того, чтобы не включать в выборку новые языки, которые уже возвращались в предыдущем ответе.
				// Мы нагло предполагаем, что пользователю не пришло в голову добавлять слова с точностью до миллисекунды (ибо нефиг),
				// а если и пришло, то надеемся, что сервер так быстро (в один момент) эти запросы не обработает.
				//
				// Если нам покажется, что это источник больших проблем, то можно просто формировать ответ новых языков и возвращать его методом addUserWords
				// Хм. Пожалуй, так и сделаем (TODO: (3.low)), но чуть позже.
				List<Language> responseLanguages = storage.getLatestUserWords(userId, lastConnection + 1);
				List<Language> requestLanguages = parseJSON(json.getJSONArray(ActionParams.LANGUAGES));
				storage.addUserWords(userId, requestLanguages, currentConnection);

				// Следующей строкой мы добываем айдишники новых языков, которые были добавлены клиентом.
				// Дело в том, что новым словам было установлено время изменения равное currentConnection и они не попадут в выборку,
				// а вот новым языкам было установлено время изменения равное currentConnection + 1;
				responseLanguages.addAll(storage.getLatestUserWords(userId, currentConnection));

				setAnswerParam(ActionParams.LANGUAGES, responseLanguages);
				setAnswerParam(ActionParams.LAST_CONNECTION, currentConnection);
				setAnswerParam(ActionParams.SUCCESS, true);
			}
			else
			{
				setAnswerParam(ActionParams.SUCCESS, false);
			}
		}
		catch(SmdException e)
		{
			log(e);
			setAnswerParam(ActionParams.SUCCESS, false);
			setAnswerParam(ActionParams.MESSAGE, e.getPublicMessage());
		}
		catch(Exception e)
		{
			log(e);
			setAnswerParam(ActionParams.SUCCESS, false);
			setAnswerParam(ActionParams.MESSAGE, SmdException.ERROR);
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
			throw new WordsException(WordsException.JSON_ERROR, e, true);
		}

		return languages;
	}

	@Override
	protected boolean validateParams (HttpServletRequest request)
	{
		return request.getParameter(ActionParams.DATA) != null;
	}
}