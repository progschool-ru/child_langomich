package org.smdme;

import javax.microedition.io.*;
import java.io.*;
import java.util.Date;
import org.json.me.*;
import com.ccg.util.JavaString;


public class Timing extends Thread
{
    private static final String ACTION_PATH = "/smdserver/servlet";
    private Languages languages;
    private Settings settings;

    private HttpConnection hc;
    private String query;
    private OutputStream os;
    private InputStream is;

    Thread t;

    private String text = "";
    Timing()
    {
        super("Timing");
    }
    public void run()
    {
            Text T = new Text();
            long currentTime = new Date().getTime();
            settings = new Settings();
            languages = new Languages();
            text = T.OPENING_A_CONNECTION;
            try
            {
                hc = (HttpConnection)Connector.open("http://"+settings.getURL() + ACTION_PATH + "/mobileLogin");// TODO: (2.medium) mobileLogin ни чем не лучше, чем просто login. Следует удалить MobileLoginAction и исползовать здесь LoginAction.
                hc.setRequestMethod(HttpConnection.POST);
                hc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

                query = "login="+settings.getLogin()+"&password="+settings.getPassword()+"";
                os = hc.openOutputStream();
                
                text = T.SENDING_DATA;
                os.write(query.getBytes());
                os.close();

                String c = hc.getHeaderField("Set-Cookie");
		hc.close();
                
                text = T.OPENING_A_CONNECTION;
                hc = (HttpConnection)Connector.open("http://"+settings.getURL() + ACTION_PATH + "/addWords");
                hc.setRequestMethod(HttpConnection.POST);
                hc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                hc.setRequestProperty("Cookie", c);

		long lastTiming = settings.getLastServerTiming();
	        query = "data="+getData(lastTiming);
                
                text = T.RECEIVE_DATA;
                os = hc.openOutputStream();
                os.write(query.getBytes());
                os.close();

                is = hc.openInputStream();
                StringBuffer b = new StringBuffer();
                long length = (long) hc.getLength();
                int ch = 0;
                if ( length != -1)
                    for (int i =0 ; i < length ; i++ )
                    {
                        if ((ch = is.read()) != -1)
                            b.append((char) ch);                           
                    }
                else
                    while ((ch = is.read()) != -1)
                    {
                            length = is.available() ;
                            b.append((char)ch);
                    }
                is.close();
                hc.close();
                
                text = T.PROCESSING;
                String str = b.toString();
                str = JavaString.decode(str);
		String result = setData(str, currentTime);
                settings.setNumberOfTiming(settings.getNumberOfTiming()+1);
                if("true".equals(result))
                {
                        text = T.TIMING_IS_SUCCESSFUL;
	                settings.setLastTiming(currentTime);
                        Dictionary dictionary = new Dictionary(null, Boolean.FALSE);
                        dictionary.deleteAllRecords();
                        dictionary.destroy();
                }
                else
                    text = T.TIMING_FAILED;
            }
            catch(IOException ioe)
            {
                text = "error - "+ioe.getMessage();
            }
            settings.destroy();
            languages.destroy();
    }
    public String getText()
    {
        return text;
    }
	
    private String getData(long serverTiming)
    {
        JSONObject main = new JSONObject();
        try
        {
            JSONArray JSONLanguages = new JSONArray();
            for(int i = 1; i <= languages.getNumRecords();i++)
            {
                JSONObject JSONLanguage = new JSONObject();
                JSONArray words = new JSONArray();
                Dictionary dictionary = new Dictionary(languages.getLanguage(i).getId(), settings.getLastTiming());
                Words allWords = dictionary.getAllWords();
                for(int j = 0; j < dictionary.getNumRecords();j++)
                {
                    JSONObject word = new JSONObject();
                    word.put("original",allWords.getOriginal(j));		
                    word.put("translation",allWords.getTranslation(j));
                    word.put("rating",allWords.getRating(j));
                    word.put("modified",allWords.getLastTiming(j));
                    words.put(word);
                }
                dictionary.destroy();

                Language language = languages.getLanguage(i);
                if(!languages.isInternalId(language.getId()))
                {
                        JSONLanguage.put("id", language.getId());
                }			
                JSONLanguage.put("name", language.getName());				
                JSONLanguage.put("words", words);
                JSONLanguages.put(JSONLanguage);//TODO: (3.low) Возложить функцию конвертирования в JSON на сам класс Language. Чтобы было как-то так: JSON.encode(language); или JSONLanguages.put(language);
            }
            main.put("languages", JSONLanguages);
            if(serverTiming > 0)
            {
                    main.put("lastConnection", serverTiming);
            }
        }
        catch(JSONException e){}
		//System.out.println("get: " + main.toString()); //Чтобы эти строки не писать, а потом не удалять, оставлю закомментированными.
        return JavaString.encode(main.toString());
    }
    private String setData(String data, long currentTime)
    {
		//System.out.println("set: " + data); //Чтобы эти строки не писать, а потом не удалять, оставлю закомментированными.
        try
        {
            JSONObject json = new JSONObject(data);
            if(!json.has("success") || !json.getBoolean("success"))
            {
                    return json.get("success").toString();
            }
			
            JSONArray JSONLanguages = json.getJSONArray("languages");
            for(int i = 0; i < JSONLanguages.length();i++)
            {
                JSONObject JSONLanguage = JSONLanguages.getJSONObject(i);
                JSONArray words = JSONLanguage.getJSONArray("words");
                String languageName = JSONLanguage.getString("name");
                String languageId = JSONLanguage.getString("id");

                Dictionary dictionary = new Dictionary(languageId);

                String oldId = languages.addLanguageAndGetOldLanguageId(languageName, languageId, dictionary);
                if(settings.getLanguage().equals("null") || settings.getLanguage().equals(oldId))
                {
                    settings.setLanguage(languageId);
                }

                for(int j = 0; j < words.length();j++)
                {
                    JSONObject word = words.getJSONObject(j);
                    dictionary.newRecord(word.getString("original"),
                    word.getString("translation"),
                    word.getInt("rating"),
                    currentTime);
                }
                dictionary.destroy();
            }
            if(json.has("lastConnection"))
            {
                    settings.setLastServerTiming(json.getLong("lastConnection"));
            }
            return  json.get("success").toString();
        }
        catch(JSONException e)
		{
                    System.out.println("error - "+e.getMessage());
			//TODO: (3.low) Предлагаю устроить в приложении хранилище 
			// последних исключений с описаниями и при синхронизации отправлять их на сервер. 
			// Так мы получим богатое подспорье для поиска и исправления багов.
			// 
			// Нужно организовать сбор этих данных во всех местах, 
			// где сейчас исключение молча проглатывается.
		}
        return null;
    }
}
