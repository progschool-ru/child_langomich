package org.omich.lang;


import java.util.ArrayList;
import java.util.List;
import org.omich.lang.httpClient.SmdClient;
import org.omich.lang.json.JSONParser;
import org.omich.lang.words.Language;

import com.ccg.util.JavaString;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class WordsActivity extends Activity  {

	private static final int UNKNOW_ERROR = 1;
	private static final int AUTH_ERROR = 2;
	
	private ListView viewRes;
	
	private String login;
	private String password;
	
	private SmdClient smdClient;
	
	private ArrayAdapter<Language> aa;
	private List<Language> wordsList = new ArrayList<Language>();
	
	private String error;
	
	//WordsDataSource dataSource;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.words);
		
		
		viewRes = (ListView) findViewById(R.id.listView1);
	
		Intent intent = getIntent();
		
		login = intent.getStringExtra(Constants.STR_LOGIN);
		password = intent.getStringExtra(Constants.STR_PASSWORD);
		

		
		int layoutID = android.R.layout.simple_list_item_1;
		
		
		
		smdClient = new SmdClient();
		try{
			
			String authResString = JavaString.decode(smdClient.auth(login, password));
			
			boolean success = JSONParser.parseAuth(authResString);
			
			if(success){
				
				long lastConnection = 0;
				
				String jString1 = JavaString.decode(smdClient.getWords());
				
				wordsList = JSONParser.parseLanguages(jString1, lastConnection);
				
				aa = new ArrayAdapter<Language>(this, layoutID, wordsList);
				viewRes.setAdapter(aa);
				
				aa.notifyDataSetChanged();
				
			}else{
				
				showDialog(AUTH_ERROR);
			}
		}catch(Exception e){
			
			error = e.getMessage();
			showDialog(UNKNOW_ERROR);
		}
	
	}
	
	public void onPause(){
		super.onPause();
	}
	
	@Override
	public Dialog onCreateDialog(int id){
		
		AlertDialog.Builder bulder = new AlertDialog.Builder(this);
		switch(id){
			
			case UNKNOW_ERROR:
				bulder.setMessage("error = "+error);
				bulder.setCancelable(false);
				bulder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						WordsActivity.this.finish();
					}
				});
				break;
			
			case AUTH_ERROR:
				bulder.setMessage("Auth error");
				bulder.setCancelable(true);
				bulder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
							WordsActivity.this.finish();
					}
				});
				break;
		}
		
		AlertDialog alert = bulder.create();
		alert.show();
		
		return super.onCreateDialog(id);
	}
	
}
