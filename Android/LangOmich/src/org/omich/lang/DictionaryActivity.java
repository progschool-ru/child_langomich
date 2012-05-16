package org.omich.lang;

import java.util.List;
import org.omich.lang.SQLite.IQueryLanguage;
import org.omich.lang.SQLite.IQueryWords;
import org.omich.lang.SQLite.MySQLiteHelper;
import org.omich.lang.SQLite.WordsStorage;
import org.omich.lang.comparators.ComparatorOriginalDown;
import org.omich.lang.comparators.ComparatorOriginalUp;
import org.omich.lang.comparators.ComparatorRatingDown;
import org.omich.lang.comparators.ComparatorRatingUp;
import org.omich.lang.comparators.comparatorTranslateDown;
import org.omich.lang.comparators.comparatorTranslateUp;

import org.omich.lang.words.Word;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

public class DictionaryActivity extends LangOmichActivity implements OnClickListener{
	
	private static final int REQUEST_EDIT_WORDS = 0;
	private static final int REQUEST_NEW_WORD = 1;
	
	private int edit_pos;
	
	ListView myListView;
	MyAdapter myAdapter;
	
	WordsStorage myData;
	IQueryLanguage currentLanguage;
	IQueryWords currentWords;
	
	boolean deleting = false;
	Button cancelDelete;
	Button deleteOk;
	
	Button sortByOriginalButton;
	int sortStateOriginal = 0;
	Button sortByTranslateButton;
	int sortStateTranslate = 0;
	Button sortByRatingButton;
	int sortStateRating = 0;
	
	MyImageButton delButton;
	MyImageButton addButton;
	
	int languageId;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dictionary);
		
		addSettings();
		
		sortByOriginalButton = (Button) findViewById(R.id.sort_by_original);
		sortByOriginalButton.setOnClickListener(this);
		
		sortByTranslateButton = (Button) findViewById(R.id.sort_by_translate);
		sortByTranslateButton.setOnClickListener(this);
		
		sortByRatingButton = (Button) findViewById(R.id.sort_by_rating);
		sortByRatingButton.setOnClickListener(this);
		
		myListView = (ListView) findViewById(R.id.listView1);
		
		deleteOk = (Button) findViewById(R.id.deleteOk);
		deleteOk.setOnClickListener(this);
		
		cancelDelete = (Button) findViewById(R.id.canceDeletel);
		cancelDelete.setOnClickListener(this);
		
		
		languageId = lSettigs.getLanguageId();
		
		myData = new WordsStorage(this);
		myData.open();
		currentLanguage = myData.getQueryLanguage(languageId);
		if(currentLanguage != null){
			currentWords = currentLanguage.getQueryWords();
			List<Word> myList = currentWords.getWords();
			myAdapter = new MyAdapter(this, myList);
			myListView.setAdapter(myAdapter);
			
		}
		myData.close();
		myListView.setOnItemLongClickListener(new OnWordClickListener());
	}
	
	public void onClick(View v){
		switch(v.getId()){
			
			case R.id.sort_by_original:
				sortByOriginal();
				break;
				
			case R.id.sort_by_translate:
				sortByTranslate();
				break;
				
			case R.id.sort_by_rating:
				sortByRating();
				break;
			case R.id.canceDeletel:
				stopDel();
			case R.id.deleteOk:
				delWords();
				break;
		}
	}
	
	private class OnWordClickListener implements OnItemLongClickListener{

		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
				
				edit_pos = position;
				
				Word word = myAdapter.getItem(position);
				
				Intent intent = new Intent(getApplicationContext(), EditWordActivity.class);
				
				intent.putExtra(MySQLiteHelper.WORD_ID, word.getId());
				intent.putExtra(MySQLiteHelper.ORIGINAL, word.getOriginal());
				intent.putExtra(MySQLiteHelper.TRANSLATION, word.getTranslation());
				intent.putExtra(MySQLiteHelper.RATING, word.getRating());
				intent.putExtra(MySQLiteHelper.MODIFIED, word.getModified());
				intent.putExtra(MySQLiteHelper.WORD_IN_SERVER, word.getInServer());
				
				intent.putExtra(MySQLiteHelper.LANGUAGE_ID, lSettigs.getLanguageId());
				
				startActivityForResult(intent, REQUEST_EDIT_WORDS);
				
			return false;
		}	
	}
	
	private class OnWordSelected implements OnItemClickListener{

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			CheckBox check = (CheckBox) arg1.findViewById(R.id.selected);
				
			if(myAdapter.addSelected(arg2)){
				check.setChecked(true);
			}else{
				check.setChecked(false);
			}
			
		}
		
	}
	@Override
	protected void onActivityResult(int requesCode, int resultCode, Intent data){
		
		if(resultCode == RESULT_OK){
			
			switch(requesCode){
				case REQUEST_EDIT_WORDS:
						Word word = IntentToWord(data);
						
				    	Word toDel = myAdapter.getItem(edit_pos); 
				    	myAdapter.add(word);
				    	myAdapter.remove(toDel);
				    	myAdapter.notifyDataSetChanged();
				 
				    break;
				case REQUEST_NEW_WORD:{
					Word newWord = IntentToWord(data);
					myAdapter.add(newWord);
					myAdapter.notifyDataSetChanged();
					break;
				}
			}
		}
		
	}
	
	@Override
	protected LinearLayout createOptionMenu(Menu menu){
			LinearLayout l = super.createOptionMenu(menu);
          
          delButton = new MyImageButton(this);
          delButton.setImageResources(R.drawable.ic_del_enable, R.drawable.ic_del_disable);
          delButton.setBackgroundColor(Color.BLACK);
          delButton.setOnClickListener(new OnDelClick());
          
          l.addView(delButton);
          
          addButton = new MyImageButton(this);
          addButton.setImageResources(R.drawable.ic_add_enabel, R.drawable.ic_add_disabel);
          addButton.setBackgroundColor(Color.BLACK);
          addButton.setOnClickListener(new OnAddClick());
          
          l.addView(addButton);
          
          return l;
	}
	private void sortByOriginal(){
		
		if(sortStateOriginal == 0 || sortStateOriginal == 2){
		     myAdapter.sort(new ComparatorOriginalDown());
		     sortStateOriginal = 1;
		}else{
			 myAdapter.sort(new ComparatorOriginalUp());
			sortStateOriginal = 2;
		}
	}
	
	private void sortByTranslate(){
		
		if(sortStateTranslate == 0 || sortStateTranslate == 2){
			  myAdapter.sort(new comparatorTranslateDown());
			sortStateTranslate = 1;
		}else{
			  myAdapter.sort(new comparatorTranslateUp());
			sortStateTranslate = 2;
		}
		
	}
	private void sortByRating(){
		
		if(sortStateRating == 0 || sortStateRating == 2){
			  myAdapter.sort(new ComparatorRatingDown());
			sortStateRating = 1;
		}else{
			  myAdapter.sort(new ComparatorRatingUp());
			sortStateRating = 2;
		}
	}
	
	private void startDel(){
		
		if(!deleting){
			sortByOriginalButton.setEnabled(false);
			sortByTranslateButton.setEnabled(false);
			sortByRatingButton.setEnabled(false);
			settingsButton.setEnabled(false);
			syncButton.setEnabled(false);
			addButton.setEnabled(false);
			cancelDelete.setVisibility(View.VISIBLE);
			deleteOk.setVisibility(View.VISIBLE);
			myAdapter.onChoceMode();
			myListView.setAdapter(myAdapter);
			myListView.setOnItemClickListener(new OnWordSelected());
			myListView.setOnItemLongClickListener(null);
		}
	}
	
	private void stopDel(){
		
		myAdapter.offChoceMode();
		myListView.setAdapter(myAdapter);
		sortByOriginalButton.setEnabled(true);
		sortByTranslateButton.setEnabled(true);
		sortByRatingButton.setEnabled(true);
		settingsButton.setEnabled(true);
		if(enabled) syncButton.setEnabled(true);
		addButton.setEnabled(true);
		myListView.setOnItemLongClickListener(new OnWordClickListener());
		cancelDelete.setVisibility(View.INVISIBLE);
		deleteOk.setVisibility(View.INVISIBLE);
		myListView.setOnItemClickListener(null);
		delButton.setEnabled(true);
	}
	
	private void delWords(){
		
		List<Word> toDel = myAdapter.getSelected(); 
		myData.open();
		currentLanguage = myData.getQueryLanguage(lSettigs.getLanguageId());
		currentWords = currentLanguage.getQueryWords();
		currentWords.deleteWords(toDel);
		myData.close();
		stopDel();
	}
	
	private Word IntentToWord(Intent data){

		long id = data.getExtras().getLong(MySQLiteHelper.WORD_ID);
		String original = data.getExtras().getString(MySQLiteHelper.ORIGINAL);
		String translation = data.getExtras().getString(MySQLiteHelper.TRANSLATION);
		int rating = data.getExtras().getInt(MySQLiteHelper.RATING);
		long modified = data.getExtras().getInt(MySQLiteHelper.MODIFIED);
		int in_server = data.getExtras().getInt(MySQLiteHelper.WORD_IN_SERVER);
		
		return new Word(id, original, translation, rating, modified, in_server);
		
	}
	
	private class OnDelClick implements OnClickListener {
		public void onClick(View v) {
			v.setEnabled(false);
			startDel();
		}
		
	}
	
	private class OnAddClick implements OnClickListener{
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(),CreateWordFastDialogActivity.class);
			intent.putExtra(MySQLiteHelper.LANGUAGE_ID, languageId);
			startActivityForResult(intent, REQUEST_NEW_WORD);
		}
		
	}
}
