package org.omich.lang;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;



public class TestActivity extends Activity {
	


	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.d("life", "onCreate");
	}
	
	 @Override
	 protected void onStart() {
	     super.onStart();
	     Log.d("life", "onStart");
	      // The activity is about to become visible.
	 }
	    
	 @Override
	 protected void onResume() {
	      super.onResume();
	      Log.d("life", "onResum");
	      // The activity has become visible (it is now "resumed").
	 }
	  
	 @Override
	 protected void onPause() {
	       super.onPause();
	       Log.d("life", "onPause");
	       // Another activity is taking focus (this activity is about to be "paused").
	 }
	    @Override
	    protected void onStop() {
	        super.onStop();
	        Log.d("life", "onStop");
	        // The activity is no longer visible (it is now "stopped")
	    }
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        Log.d("life", "onDestroy");
	        // The activity is about to be destroyed.
	    }
	
}