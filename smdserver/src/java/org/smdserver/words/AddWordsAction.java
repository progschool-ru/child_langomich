package org.smdserver.words;

import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.smdserver.actionssystem.ActionParams;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import org.smdserver.actionssystem.ActionException;
import org.smdserver.actionssystem.ParamsValidator;
import org.smdserver.auth.CheckLoginAction;
import org.smdserver.core.small.SmdException;

public class AddWordsAction extends CheckLoginAction
{	
	JSONObject json;
	
	protected String doAction (HttpServletRequest request) throws SmdException
	{
		long lastConnection;
		
		try
		{
			lastConnection = json.has(ActionParams.LAST_CONNECTION) 
							  ? json.getLong(ActionParams.LAST_CONNECTION) 
							  : 0;
		}
		catch(JSONException e)
		{
			log(e);
			lastConnection = 0;
		}
		
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
			
			
			List<Language> requestLanguages = null;
			
			if(json.has(ActionParams.LANGUAGES))
			{
				try
				{
					JSONArray languagesArray = json.getJSONArray(ActionParams.LANGUAGES);
					requestLanguages = parseJSON(languagesArray);
				}
				catch (JSONException e)
				{
					log(e);
					setAnswerSuccess(false);
					setAnswerMessage("'languages' must be correct JSON Array");
				}
			}
			else
			{
				requestLanguages = new  ArrayList<Language>();
			}
			
			if(requestLanguages != null)
			{
				storage.addUserWords(userId, requestLanguages, currentConnection);

				// Следующей строкой мы добываем айдишники новых языков, которые были добавлены клиентом.
				// Дело в том, что новым словам было установлено время изменения равное currentConnection и они не попадут в выборку,
				// а вот новым языкам было установлено время изменения равное currentConnection + 1;
				responseLanguages.addAll(storage.getLatestUserWords(userId, currentConnection));

				setAnswerParam(ActionParams.LANGUAGES, responseLanguages);
				setAnswerParam(ActionParams.LAST_CONNECTION, currentConnection);
				setAnswerSuccess(true);
			}
		}
		else
		{
			setAnswerSuccess(false);
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
	protected boolean validateParams (HttpServletRequest request) throws ActionException
	{
		ParamsValidator v = new ParamsValidator(request);
		
		v.checkNotNull(ActionParams.DATA);
		json = v.getJSONObject(ActionParams.DATA);
		
		return true;
	}
}