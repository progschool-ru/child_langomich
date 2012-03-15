package org.omich.lang;

import java.util.List;

import org.omich.lang.SQLite.LanguagesData;
import org.omich.lang.httpClient.SmdClient;
import org.omich.lang.json.JSONParser;
import org.omich.lang.words.Language;

import com.ccg.util.JavaString;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LangOmichActivity  extends FragmentActivity{
	
	protected static final String SETTINGS_NAME = "langOmich_settings";
	
	protected boolean settings = false;
	
	protected LangOmichSettings lSettigs;
	
	private ProgressDialog dialog;
	protected LanguagesData langData;
	
	private OnClickListener onSettingsClick = new OnClickListener(){
		public void onClick(View v) {
			startSettigs();
		}
	};
	
	private OnClickListener onSyncClick = new OnClickListener() {
		public void onClick(View v) {
		Synchronize sync = new Synchronize();
		sync.execute(new Void[]{});
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		lSettigs = new LangOmichSettings(this, SETTINGS_NAME);
		langData = new LanguagesData(this);
	}
	
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	  MenuItem item = menu.add(0, android.R.id.copy, 0, "Menu");
    	  
          final int twentyDp = (int) (20 * getResources().getDisplayMetrics().density);

          LinearLayout l = new LinearLayout(this);
          l.setPadding(twentyDp, 0, twentyDp, 0);
          
          TextView sync = new TextView(this);
          sync.setText("Sync");
          sync.setGravity(Gravity.CENTER);
          sync.setPadding(5, 0, 5, 0);
          sync.setOnClickListener(onSyncClick);
          l.addView(sync);
          
          if(settings){
        	  TextView tv = new TextView(this);
        	  tv.setText("Settigs");
        	  tv.setGravity(Gravity.CENTER);
        	  tv.setOnClickListener(onSettingsClick);
        	  l.addView(tv);
          }
          

          item.setActionView(l);
          item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
          
          return super.onCreateOptionsMenu(menu);
	}
    
    private  void startSettigs(){
    	Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
    
    protected void addSettings(){
    	settings = true;
    }
    
    private class Auth extends AsyncTask<String, Void, Boolean>{
    	
    	@Override
    	protected Boolean doInBackground(String... auth){
    		return true;
    	}
    }
    
    private class Synchronize extends AsyncTask<Void, Void, Void>{
    	
    	@Override
    	protected void onPreExecute(){
    		startProgressDialog();
    	}
    	
    	@Override
    	protected Void doInBackground(Void... params){
    		SmdClient client = new SmdClient();
    		try {
    			String login = lSettigs.getLogin();
    			String password = lSettigs.getPassword();
    			
    			client.auth(login, password);
    			String words = JavaString.decode(client.getWords());
    			List<Language> languages =   JSONParser.parseLanguages(words);
    			langData.open();
    			langData.createLanguages(languages);
    			langData.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
    		return null;
    	}
    	
    	@Override
    	protected void onCancelled(Void object){
    		dialog.dismiss();
    	}
    	
    	@Override 
    	protected void onPostExecute(Void result){
    		dialog.dismiss();
    	}
    	
    	 
    }
    
    private void startProgressDialog(){
		dialog = ProgressDialog.show(this, "", "Синхронизация");
    }
}
