import javax.microedition.io.*;
import java.io.*;
import java.util.Date;
import org.json.me.*;
import com.ccg.util.JavaString;


public class Timing extends Thread
{
	private static final String ACTION_PATH = "/smdserver/servlet";
    private Dictionary dictionary;
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
            dictionary = new Dictionary();
            languages = new Languages();

            try
            {
                hc = (HttpConnection)Connector.open("http://"+settings.getURL() + ACTION_PATH + "/mobileLogin");
                hc.setRequestMethod(HttpConnection.POST);
                hc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

                query = "login="+settings.getLogin()+"&password="+settings.getPassword()+"";
                os = hc.openOutputStream();
                os.write(query.getBytes());
                os.close();

                String c = hc.getHeaderField("Set-Cookie");
				hc.close();

                hc = (HttpConnection)Connector.open("http://"+settings.getURL() + ACTION_PATH + "/addWords");
                hc.setRequestMethod(HttpConnection.POST);
                hc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                hc.setRequestProperty("Cookie", c);

	            query = "data="+getData(settings.getDeviceId());

                os = hc.openOutputStream();
                os.write(query.getBytes());
                os.close();

                is = hc.openInputStream();
                int length = (int) hc.getLength();
                byte[] buff = new byte[length];
                is.read(buff);
                is.close();
				hc.close();

                text = "success - " + setData(JavaString.decode(new String(buff)), currentTime);
                settings.setNumberOfTiming(settings.getNumberOfTiming()+1);
                settings.setLastTiming(currentTime);
                settings.setText(text);
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
	
	private String getData(String deviceId)
	{
        JSONObject main = new JSONObject();
        try
        {
            JSONArray JSONLanguages = new JSONArray();
            for(int i = 1; i <= languages.getNumRecords();i++)
            {
                JSONObject JSONLanguage = new JSONObject();
                JSONArray words = new JSONArray();
                dictionary = new Dictionary(languages.getLanguage(i).getId(), settings.getLastTiming());
                for(int row = 1; row <= dictionary.getNumRecords();row++)
                {
                    JSONObject word = new JSONObject();
                    word.put("original",dictionary.getCell(row, dictionary.ORIGINAL));//TODO: (2.medium)
					// Всё-таки получается, что мы в разных местах (ещё и в SmartDictionary)
					// завязаны на особенности работы с Records, надо вызывать getCell с двумя аргументами
					// на то, что слово состоит из множества ячеек, на порядок этих ячеек.
					//
					// 1. всякие методы: getCell, getRecord и т.д. следует сделать protected
					// 2. Dictionary должен предоставлять в своём интерфейсе метод public Word getWord(int row)
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
					// 4. И пожалуйс стоит создать аналог getRecord, который возвращал бы массив String[] всех ячеек,
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
			
                    word.put("translation",dictionary.getCell(row, dictionary.TRANSLATION));
                    word.put("rating",Integer.parseInt(dictionary.getCell(row, dictionary.RATING)));
                    word.put("modified",Long.parseLong(dictionary.getCell(row, dictionary.LAST_TIMING)));
                    words.put(word);
                }

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
			main.put("deviceId", deviceId);
        }
        catch(JSONException e){}
        return JavaString.encode(main.toString());
    }
    private String setData(String data, long currentTime)
	{
        try
        {
            JSONObject json = new JSONObject(data);
            JSONArray JSONLanguages = json.getJSONArray("languages");
            for(int i = 0; i < JSONLanguages.length();i++)
            {
                JSONObject JSONLanguage = JSONLanguages.getJSONObject(i);
                JSONArray words = JSONLanguage.getJSONArray("words");
                String languageName = JSONLanguage.getString("name");
				String languageId = JSONLanguage.getString("id");
                if(settings.getLanguage().equals("null"))
                    settings.setLanguage(languageId);
                languages.addLanguage(languageName, languageId);
                dictionary = new Dictionary(languageId);//TODO: (2.medium) Вообще, я бы попробовал избавиться
				//  от повсеместного создания этих хранилищ.
				// Пусть бы они создавались один раз в одном месте и передавались бы дочерним классам через параметры.
				// Если у нас Dictionary один на всю программу, то мы бы могли наладить кэширование,
				// например, через java.util.Hashtable. Что дало бы нам выигрыш в скорости при доступе к использованным ранее данным.
                for(int j = 0; j < words.length();j++)
                {
                    JSONObject word = words.getJSONObject(j);
                    dictionary.newRecord(word.getString("original"),
							             word.getString("translation"),
										 word.getInt("rating"),
										 currentTime);
                }
            }
			if(json.has("deviceId"))
			{
				settings.setDeviceId(json.getString("deviceId"));
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
