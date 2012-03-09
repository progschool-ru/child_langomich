package org.omich.lang;


import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener {
    /** Called when the activity is first created. */
   
	
	private Button testButton;
	private Button addWordButton;
	private Button dictionaryButton;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        testButton = (Button) findViewById(R.id.startTest);
        addWordButton = (Button) findViewById(R.id.addWord);
        dictionaryButton = (Button) findViewById(R.id.startDictionary);
        
        testButton.setOnClickListener(this);
        addWordButton.setOnClickListener(this);
        dictionaryButton.setOnClickListener(this);
    }
    
    public void onClick(View g){
    	
    	switch(g.getId()){
    		
    		case R.id.startTest:
    			break;
    		case R.id.addWord:
    			break;
    		case R.id.startDictionary:
    			break;
    	}
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	  MenuItem item = menu.add(0, android.R.id.copy, 0, "Menu");

          final int twentyDp = (int) (20 * getResources().getDisplayMetrics().density);

          TypedArray a = getTheme().obtainStyledAttributes(R.styleable.SherlockTheme);
          final int abHeight = a.getLayoutDimension(R.styleable.SherlockTheme_abHeight, LayoutParams.FILL_PARENT);
          a.recycle();

          LinearLayout l = new LinearLayout(this);
          l.setPadding(twentyDp, 0, twentyDp, 0);

          TextView tv = new TextView(this);
          tv.setText("Settigs");
          tv.setGravity(Gravity.CENTER);
          tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, abHeight));
          l.addView(tv);

          l.setOnClickListener(new View.OnClickListener() {
            
              public void onClick(View v) {
            	  startSettigs();
              }
          });

          item.setActionView(l);
          item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

          return super.onCreateOptionsMenu(menu);
	}
    
    private  void startSettigs(){
    	Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
}