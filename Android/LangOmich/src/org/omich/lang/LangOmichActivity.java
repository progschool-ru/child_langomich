package org.omich.lang;


import org.omich.lang.SQLite.LanguagesData;
import org.omich.lang.httpClient.SmdClient;
import org.omich.lang.json.JSONParser;

import com.ccg.util.JavaString;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LangOmichActivity  extends FragmentActivity{
	
	protected static final String SETTINGS_NAME = "langOmich_settings";
	
	protected boolean settings = false;
	
	protected LangOmichSettings lSettigs;
	
	protected LanguagesData langData;
	
	protected ImageButton syncButton;
	
	private OnClickListener onSettingsClick = new OnClickListener(){
		public void onClick(View v) {
			startSettigs();
		}
	};
	
	private OnClickListener onSyncClick = new OnClickListener() {
		public void onClick(View v) {			
			startSuncServise();
		}
	};
	
	private void startSuncServise(){
		Intent intent = new Intent(this, SyncService.class);
		startService(intent);
	}
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
          
          syncButton = new ImageButton(this);
          syncButton.setPadding(5, 0, 5, 0);
          syncButton.setOnClickListener(onSyncClick);
          l.addView(syncButton);
          
          if(settings){
        	  TextView tv = new TextView(this);
        	  tv.setText("Settigs");
        	  tv.setGravity(Gravity.CENTER);
        	  tv.setOnClickListener(onSettingsClick);
        	  tv.setEnabled(false);
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
    
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return  activeNetworkInfo != null;
    }
    
    protected void updataSuncButton(){
    	if (isNetworkAvailable() ){
    		syncButton.setEnabled(true);
    	}else{
    		syncButton.setEnabled(false);
    	}
    
    	
		AsyncAuth auth = new AsyncAuth();
		auth.execute(new Void [] {});
    }
    
    protected void  updateUIAfteAsyncAuth(Boolean result){
    	syncButton.setEnabled(result);
    }
    
    protected class AsyncAuth extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected Boolean doInBackground(Void... params){
			SmdClient client = new SmdClient();
			
			boolean authres;
			
			try {
				String login_s = lSettigs.getLogin();
				String password_s = lSettigs.getPassword();
				String result = client.auth(login_s, password_s);
				String jString = JavaString.decode(result);
				authres = JSONParser.parseAuth(jString);
			} catch (Exception e) {
				authres = false;
			}
		
			return authres;
		}
		
		@Override
		protected void onPostExecute(Boolean result){
			updateUIAfteAsyncAuth(result);
		}
	}
}
