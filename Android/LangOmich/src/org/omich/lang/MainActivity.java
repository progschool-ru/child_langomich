package org.omich.lang;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
    }
    
    public void onClick(View g){
    
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    }
}