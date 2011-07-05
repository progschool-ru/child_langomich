import javax.microedition.io.*;
import java.io.*;
import java.util.Date;
import org.json.me.*;

public class Timing extends Thread
{
    private Dictionary dictionary;
    private Languages languages;
    private Settings settings;

    private HttpConnection hc;
    private String query;
    private OutputStream os;
    private InputStream is;

    Thread t;

    Timing()
    {
        super("Timing");
        start();
    }
    public void run()
    {
            settings = new Settings();
            dictionary = new Dictionary();
            languages = new Languages();
            String test = "";
            try
            {
                hc = (HttpConnection)Connector.open("http://"+settings.getURL()+"/smdserver/action/mobileLogin");
                hc.setRequestMethod(HttpConnection.POST);
                hc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                query = "login="+settings.getLogin()+"&password="+settings.getPassword()+"";
                os = hc.openOutputStream();
                os.write(query.getBytes());
                os.close();

                String c = hc.getHeaderField("Set-Cookie");

                hc = (HttpConnection)Connector.open("http://"+settings.getURL()+"/smdserver/action/mobileAddWords");
                hc.setRequestMethod(HttpConnection.POST);
                hc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                hc.setRequestProperty("Cookie", c);

                query = "data="+getData().toString();

                os = hc.openOutputStream();
                os.write(query.getBytes());
                os.close();

                is = hc.openInputStream();
                int length = (int) hc.getLength();
                byte[] buff = new byte[length];
                is.read(buff);
                is.close();
                test = test + setData(new String(buff));
                settings.setNumberOfTiming(settings.getNumberOfTiming()+1);
                settings.setLastTiming(new Date().getTime());
            }
            catch(IOException ioe){}
    }
    private JSONObject getData(){
        JSONObject main = new JSONObject();
        try
        {
            JSONArray JSONLanguages = new JSONArray();
            for(int i = 1; i <= languages.getNumRecords();i++)
            {
                JSONObject JSONLanguage = new JSONObject();
                JSONArray words = new JSONArray();
                dictionary = new Dictionary(languages.getLanguage(i), settings.getLastTiming());
                for(int row = 1; row <= dictionary.getNumRecords();row++)
                {
                    JSONObject word = new JSONObject();
                    word.put("original",dictionary.getCell(row, dictionary.ORIGINAL));
                    word.put("translation",dictionary.getCell(row, dictionary.TRANSLATION));
                    word.put("rating",Integer.parseInt(dictionary.getCell(row, dictionary.RATING)));
                    word.put("modified",Long.parseLong(dictionary.getCell(row, dictionary.LAST_TIMING)));
                    words.put(word);
                }
                JSONLanguage.put("name", languages.getLanguage(i));
                JSONLanguage.put("words", words);
                JSONLanguages.put(JSONLanguage);
            }
            main.put("languages", JSONLanguages);
            main.put("lastModified", settings.getLastTiming());
            main.put("numberOfTiming", settings.getNumberOfTiming());
        }
        catch(JSONException e){}
        return main;
    }
    private String setData(String data){
        try
        {
            JSONObject json = new JSONObject(data);
            JSONArray JSONLanguages = json.getJSONArray("languages");
            System.out.println(JSONLanguages.length());
            for(int i = 0; i < JSONLanguages.length();i++)
            {
                JSONObject JSONLanguage = JSONLanguages.getJSONObject(i);
                JSONArray words = JSONLanguage.getJSONArray("words");
                String name = JSONLanguage.get("name").toString();
                if(settings.getLanguage().equals("null"))
                    settings.setLanguage(name);
                languages.newLanguage(name);
                dictionary = new Dictionary(name);
                for(int j = 0; j < words.length();j++)
                {
                    JSONObject word = words.getJSONObject(j);
                    dictionary.newRecord(word.getString("original"),word.getString("translation"),word.getInt("rating"),word.getLong("modified"));
                }
            }
            return  json.get("success").toString();
        }
        catch(JSONException e){}
        return null;
    }
}
