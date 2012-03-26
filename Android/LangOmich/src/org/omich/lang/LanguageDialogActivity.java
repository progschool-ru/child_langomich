package org.omich.lang;
import java.util.List;

import org.omich.lang.SQLite.LanguagesData;
import org.omich.lang.words.Language;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


public class LanguageDialogActivity extends ListActivity{

	    public void onCreate(Bundle icicle) {
	        super.onCreate(icicle);
	        
	        LanguagesData myData;
	        
	        myData = new LanguagesData(this);
			myData.open();
			List<Language> myList = myData.getListAllLanguages();
			
			myData.close();
	        
	        ListAdapter adapter = new ArrayAdapter<Language>(this,
	                android.R.layout.simple_list_item_1, myList);
	        setListAdapter(adapter);
	    }

	    @Override
	    protected void onListItemClick(ListView l, View v, int position, long id) {
	    	Language lang =  (Language) getListAdapter().getItem(position);
	    	Intent data= new Intent();
	    	data.putExtra(LangOmichSettings.LANGUAGE_NAME, lang.getName());
	    	data.putExtra(LangOmichSettings.LANGUAGE_ID, lang.getId() );
	    	setResult(RESULT_OK, data);
	        finish();
	    }
}
