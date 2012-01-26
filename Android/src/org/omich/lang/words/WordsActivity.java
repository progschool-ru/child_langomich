package org.omich.lang.words;


import android.app.Activity;
import android.os.Bundle;

import org.omich.lang.R;

public class WordsActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.words);
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
}
