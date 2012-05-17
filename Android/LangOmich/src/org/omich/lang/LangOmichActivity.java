package org.omich.lang;

import org.omich.lang.httpClient.SmdClient;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class LangOmichActivity  extends FragmentActivity{
	
	protected static final String SETTINGS_NAME = "langOmich_settings";
	 
	protected boolean settings = false;
	
	protected LangOmichSettings lSettigs;
	
	protected MyImageButton syncButton;
	protected MyImageButton settingsButton;
	protected boolean enabled;
	
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
		lSettigs.edit();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(syncButton != null){
			 updataSuncButton();
		}
	}
	
	@Override
	public void onStop(){
		super.onStop();
		lSettigs.saveEnabledSyncButton(enabled);
	}
	
    @Override
	public boolean onCreateOptionsMenu(Menu menu){
    		createOptionMenu(menu);
          return super.onCreateOptionsMenu(menu);
	}
    
    protected LinearLayout createOptionMenu(Menu menu){
    	  MenuItem item = menu.add(0, android.R.id.copy, 0, "Menu");
    	  
    		 
          final int twentyDp = (int) (20 * getResources().getDisplayMetrics().density);

          LinearLayout l = new LinearLayout(this);
          l.setPadding(twentyDp, 0, twentyDp, 0);
          
          syncButton = new MyImageButton(this);
          syncButton.setPadding(5, 0, 5, 0);
          enabled = lSettigs.getEnabledSyncButton();
          syncButton.setEnabled(enabled);
          updataSuncButton();
          syncButton.setImageResources(R.drawable.ic_sunc_enable, R.drawable.ic_sunc_disable);
          syncButton.setBackgroundColor(Color.BLACK);
          syncButton.setOnClickListener(onSyncClick);
          
         
          l.addView(syncButton);
          
          if(settings){
        	  settingsButton = new MyImageButton(this);
        	  settingsButton.setBackgroundColor(Color.BLACK);
        	  settingsButton.setEnabled(true);
        	  settingsButton.setImageResources(R.drawable.ic_settings_enable, R.drawable.ic_settings_disable);
        	  settingsButton.setOnClickListener(onSettingsClick);
        	  l.addView(settingsButton);
          }
          
          item.setActionView(l);
          item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
          
          return l;
    	
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
    	
    	if (!isNetworkAvailable() ){
    		enabled = false;
    		syncButton.setEnabled(enabled);
    		return;
    	}
    
		AsyncAuth auth = new AsyncAuth();
		auth.execute(new Void [] {});
    }
    
 
    
    protected boolean auth(){
    	SmdClient client = new SmdClient();
		
		boolean authres;
		
		try {
			String login_s = lSettigs.getLogin();
			String password_s = lSettigs.getPassword();
			authres = client.auth(login_s, password_s);
		 
		} catch (Exception e) {
			authres = false;
		}
	
		return authres;
    	
    }
    
    private class AsyncAuth extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected Boolean doInBackground(Void... params){
			return auth();
		}
		
		@Override
		protected void onPostExecute(Boolean result){
			enabled = result;
			syncButton.setEnabled(enabled);
		}
	}
}
