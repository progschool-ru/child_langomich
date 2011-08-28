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
        start();
    }
    public void run()
    {
			long currentTime = new Date().getTime();
            settings = new Settings(); // TODO: (2.medium) Вопрос. Не нужно ли этим объектам вызывать destroy() в конце метода?
            languages = new Languages();

            try
            {
                hc = (HttpConnection)Connector.open("http://"+settings.getURL() + ACTION_PATH + "/mobileLogin");// TODO: (2.medium) mobileLogin ни чем не лучше, чем просто login. Следует удалить MobileLoginAction и исползовать здесь LoginAction.
                hc.setRequestMethod(HttpConnection.POST);
                hc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

                query = "login="+settings.getLogin()+"&password="+settings.getPassword()+"";
                os = hc.openOutputStream();
                os.write(query.getBytes());
                os.close();

                String c = hc.getHeaderField("Set-Cookie");
				hc.close();

                hc = (HttpConnection)Connector.open("http://"+settings.getURL() + ACTION_PATH + "/addWords");// TODO: (1.high) Попытки загрузить более 20 слов по JPRS почти всегда неудачны. Выяснить с чем это связано. Попытаться исправить, ведь OperaMini может грузить достаточно большие страницы, значит и мы можем. (возможно, для этого придётся доработать серверный API)
                hc.setRequestMethod(HttpConnection.POST);
                hc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                hc.setRequestProperty("Cookie", c);

				long lastTiming = settings.getLastServerTiming();
	            query = "data="+getData(lastTiming);

                os = hc.openOutputStream();
                os.write(query.getBytes());
                os.close();

                is = hc.openInputStream();
                int length = (int) hc.getLength();
                byte[] buff = new byte[length];
                is.read(buff);
                is.close();
				hc.close();

				String result = setData(JavaString.decode(new String(buff)), currentTime);
                text = "success - " + result;
                settings.setNumberOfTiming(settings.getNumberOfTiming()+1);
                settings.setText(text);
				
				if("true".equals(result))
				{
	                settings.setLastTiming(currentTime);
					Dictionary dictionary = new Dictionary(null, Boolean.FALSE);
					dictionary.deleteAllRecords();
				}
            }
            catch(IOException ioe)
            {
                text = "error - "+ioe.getMessage();
                settings.setText(text);
            }
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
                for(int row = 1; row <= dictionary.getNumRecords();row++)
                {
                    JSONObject word = new JSONObject();
                    word.put("original",dictionary.getCell(row, Dictionary.ORIGINAL));//TODO: (2.medium)
					// Всё-таки получается, что мы в разных местах (ещё и в SmartDictionary)
					// завязаны на особенности работы с Records, надо вызывать getCell с двумя аргументами
					// на то, что слово состоит из множества ячеек, на порядок этих ячеек.
					//
					// 1. всякие методы: getCell, getRecord и т.д. следует сделать protected
					// 2. Dictionary должен предоставлять в своём интерфейсе метод public Word getWord(int row);
					// 3. В классе Word определить методы getText, getOriginal и т.д. 
					//     Тогда точно ни у кого не будет соблазна написать dictionary.getCell(row, 3);
					//     Тогда не приходится вспоминать, что это getCell означает и как им пользоваться.
					//
					//     Кроме того, получаем дополнительный бонус.
					//     При каждом вызове getCell (тут это от нас скрыто, но мы то знаем),
					//     так вот, при каждом вызове вызывается getId, который пробегает циклом по
					//     всем записям и ищет нужный Id. 
					//     Чтобы получить 4 поля, мы пробелгаем 4 одинаковых цикла в getId и перестраиваем re.
					//     В случае getWord, мы это будем делать только один раз.
					// 4. И пожалуй стоит создать аналог getRecord, который возвращал бы массив String[] всех ячеек,
					//    которые сохранены в записи. 
					//    Тогда внутри класса Dictionary, для того чтобы построить слово Word,
					//    нам придётся обращаться к Records не три раза, а один.
					// 5. Это же отчасти касается других словарей: Languages и отчасти Settings.
					//    Точнее, так получилось, что там уже сделано всё что нужно. 
					//    Все низкоуровневые методы класса Records оказались перекрыты методами:
					//    getLanguage(int) (хоть он и возвращает просто строку, но от него больше и не требуется, наш язык - это строка)
					//    ещё один getLanguage(), getNumberOfWords(), getLastTiming() и т.д.
					//    Это прекрасные примеры того, как мы завернули низкоуровневый Records
					//    в более умные классы Languages и Settings.
					//
					//PS. Подобные комментарии помечаются словом TODO, обычно комментарии бывают короче, но не важно.
					// Их легко искать поиском по слову TODO. Я ещё придумал добавлять к ним приоритет,
					// чтобы различать критичные действия от просто пожеланий на будущее.
					// Когда комментарий потерял актуальность (устарел или был выполнен), его следует удалить.
					//
			
                    word.put("translation",dictionary.getCell(row, Dictionary.TRANSLATION));
                    word.put("rating",Integer.parseInt(dictionary.getCell(row, Dictionary.RATING)));
                    word.put("modified",Long.parseLong(dictionary.getCell(row, Dictionary.LAST_TIMING)));
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
