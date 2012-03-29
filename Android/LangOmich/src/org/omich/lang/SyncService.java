package org.omich.lang;

import java.util.List;

import org.omich.lang.SQLite.LanguagesData;
import org.omich.lang.httpClient.SmdClient;
import org.omich.lang.json.JSONParser;
import org.omich.lang.json.JSONWriter;
import org.omich.lang.words.Language;

import com.ccg.util.JavaString;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SyncService extends Service {
	
	LangOmichSettings lSettings;
	
	private LanguagesData langData;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() { 
        lSettings = new LangOmichSettings(this,LangOmichActivity.SETTINGS_NAME);
        langData = new LanguagesData(this);

    }

    @Override
    public void onDestroy() {
        
     
    }

    @Override
    public void onStart(Intent intent, int startid) {
    	 
    	SmdClient client = new SmdClient();
    	try{
      
        String login = lSettings.getLogin();
		String password = lSettings.getPassword();
		client.auth(login, password);
		
		long lastConnection = lSettings.getLastConnect();
		String data = JSONWriter.toJSON(lastConnection, null);
		Log.d("test", data);
		
		data = JavaString.encode(data);
		String words = JavaString.decode(client.addWords(data));
		List<Language> languages =   JSONParser.parseLanguages(words);
	
		langData.open(); 
		langData.createLanguages(languages);
		langData.close();
		
		Log.d("test", words);
		lastConnection = JSONParser.getLastConnection(words);
		lSettings.saveLastConnection(lastConnection);
		
    	}catch (Exception e) {
			e.printStackTrace();
		}
    }
}