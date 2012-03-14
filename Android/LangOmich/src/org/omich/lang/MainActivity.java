package org.omich.lang;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
public class MainActivity extends LangOmichActivity implements OnClickListener {
    /** Called when the activity is first created. */
   
	
	private Button testButton;
	private Button addWordButton;
	private Button dictionaryButton;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        addSettings();
        
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
    			startActivity(new Intent(this, DictionaryActivity.class));
    			break;
    	}
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    }
    
}