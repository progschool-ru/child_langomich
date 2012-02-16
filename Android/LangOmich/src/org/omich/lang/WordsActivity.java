package org.omich.lang;


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.omich.lang.SQLite.WordsDataSource;
import org.omich.lang.httpClient.SmdClient;
import org.omich.lang.words.Word;
import org.omich.lang.words.Words;

import com.ccg.util.JavaString;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class WordsActivity extends Activity  {

	private static final int UNKNOW_ERROR = 1;
	private static final int AUTH_ERROR = 2;
	
	private ListView viewRes;
	
	private String login;
	private String password;
	
	private SmdClient smdClient;
	
	private ArrayAdapter<Word> aa;
	private List<Word> wordsList = new ArrayList<Word>();
	
	private String error;
	
	WordsDataSource dataSource;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.words);
		
		
		viewRes = (ListView) findViewById(R.id.listView1);
	
		Intent intent = getIntent();
		
		login = intent.getStringExtra(Constants.STR_LOGIN);
		password = intent.getStringExtra(Constants.STR_PASSWORD);
		

		
		int layoutID = android.R.layout.simple_list_item_1;
		aa = new ArrayAdapter<Word>(this, layoutID, wordsList);
		
		viewRes.setAdapter(aa);
		
		smdClient = new SmdClient();
		try{
			String authResString = JavaString.decode(smdClient.auth(login, password));
			AuthResParser authRes = new AuthResParser(authResString);
			if(authRes.getSuccess()){
				String jString = JavaString.decode(smdClient.getWords());
				Words words = new Words(jString);
			
				for(int i=0; i<words.length(); i++){
					Word word = words.getWord(i);
					wordsList.add(word);
				}
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
	
	public void addToBase(View g){
		dataSource = new WordsDataSource(this);
		dataSource.open();
		dataSource.deleteAllWords();
		ListIterator<Word> iter = wordsList.listIterator();
		while (iter.hasNext()){
			Word _word = iter.next();
			dataSource.createWord(_word);
		}
		dataSource.close();
	}
}
