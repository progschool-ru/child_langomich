package org.omich.lang;


import org.omich.lang.SQLite.MySQLiteHelper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class TestActivity extends Activity {
	
	private TextView textView;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_view);
		
		MySQLiteHelper sHelper = new MySQLiteHelper(this);
		sHelper.getWritableDatabase();
	}
	

	
	@Override
	public void onPause(){
		super.onPause();
	}
		
}