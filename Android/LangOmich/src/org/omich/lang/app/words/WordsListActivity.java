package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.PreferenceFields;
import org.omich.lang.app.db.Dict;
import org.omich.lang.app.db.Word;
import org.omich.lang.app.words.WordsListAdapter;
import org.omich.lang.apptool.activity.ABActivity;
import org.omich.tool.events.Listeners.IListener;
import org.omich.tool.events.Listeners.IListenerVoid;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class WordsListActivity extends ABActivity
{
	private final int REQUEST_CODE_ADD_DICT = 101;
	private final int REQUEST_CODE_ADD_WORD = 102;
	private final int REQUEST_CODE_EDIT_WORD = 103;
	
	private WordsListAdapter mWordsAdapter;
	
	private Animation animationSideIn;
	private Animation animationSideOut;
	private View sideScreen;
	private View returnButton;
	
	private Word word;
	
	private String mCopyWordTaskId;
	private String mCutWordTaskId;
	private String mDeleteWordTaskId;
	
	private SharedPreferences sp;	
	private DictsListAdapter mDictsAdapter;
	private boolean mIsDestroyed;
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_wordslist);	
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		ListView lv = (ListView)findViewById(R.id.wordslist_list);
	
		mWordsAdapter = new WordsListAdapter(this, getBcConnector(), 
				sp.getLong(PreferenceFields.DICT_ID, -1));
		mWordsAdapter.reloadItems();
		
		lv.setAdapter(mWordsAdapter);

		mDictsAdapter = new DictsListAdapter(this, getBcConnector(), true, new IListenerVoid()
		{
			public void handle ()
			{ 
				setSelectedPosition();	
			}			
		});
		mDictsAdapter.reloadItems();
		
		getActionBar().setListNavigationCallbacks(mDictsAdapter, new OnNavigationListener() 
		{
			public boolean onNavigationItemSelected(int position, long itemId) 
			{
        		int size = mDictsAdapter.getCount();
        		if(position + 1 == size) 
        			startAddDictActivity();    			
        		else
        			onSelectDict(position);				
				return true;
			}
		});	
		
		animationSideIn = AnimationUtils.loadAnimation(this, R.anim.side_in);
		animationSideOut = AnimationUtils.loadAnimation(this, R.anim.side_out);
		
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				if(mWordsAdapter.getSelectedPosition() != -1 && sideScreen != null)
				{
					sideScreen.startAnimation(animationSideOut);
					sideScreen.setVisibility(View.INVISIBLE);
					returnButton.setVisibility(View.INVISIBLE);
				}
				
				word = (Word)mWordsAdapter.getItem(position);
				mWordsAdapter.setSelectedPosition(position);

				sideScreen = view.findViewById(R.id.item_wordlist_screen_side); 
				sideScreen.setVisibility(View.VISIBLE);
				sideScreen.startAnimation(animationSideIn);
				
				returnButton = view.findViewById(R.id.item_wordlist_button_return); 
				returnButton.setVisibility(View.VISIBLE);

				mWordsAdapter.notifyDataSetChanged();
			}
		});
	}
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		  MenuInflater inflater = getMenuInflater();
		  inflater.inflate(R.menu.menu_words_list, menu);	

		  return super.onCreateOptionsMenu(menu);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		if(id == R.id.app_menu_item_button_add_word)
		{
		    Intent intent = new Intent(this, AddWordActivity.class);
		    startActivityForResult(intent, REQUEST_CODE_ADD_WORD);				
		}
		else if(id == R.id.app_menu_item_button_game)
		{
			startActivity(new Intent(this, GameActivity.class));
			finish();
		}
		return true;
    }		
	public void onEdit (View v)
	{
		if(word == null)
			return;
		Intent intent = new Intent(this, EditWordActivity.class);
		intent.putExtra("id", word.id);
		intent.putExtra("nativ", word.nativ);
		intent.putExtra("foreign", word.foreign);
		startActivityForResult(intent, REQUEST_CODE_EDIT_WORD);
	}	
	public void onReturn (View v)
	{		
		if(mWordsAdapter.getSelectedPosition() == -1)
			return;
		sideScreen.startAnimation(animationSideOut);
		sideScreen.setVisibility(View.INVISIBLE);
		returnButton.setVisibility(View.INVISIBLE);
		mWordsAdapter.setSelectedPosition(-1);
	}	
	public void onDelete (View v)
	{
		if(word == null || mDeleteWordTaskId != null)
			return;

		String successText = getResources().getString(R.string.wordslist_report_deleted);
		
		Intent intent = DeleteWordTask.createIntent(word.id, successText);
		mDeleteWordTaskId = getBcConnector().startTypicalTask(DeleteWordTask.class, 
				intent, 
				new IListener<Bundle>()
				{
					public void handle (Bundle bundle)
					{
						if(mIsDestroyed)
							return;
				
						mDeleteWordTaskId = null;
						
						mWordsAdapter.setSelectedPosition(-1);
						mWordsAdapter.reloadItems();
					}
				});
	}	
	private void setSelectedPosition()
	{
		long dictId = sp.getLong(PreferenceFields.DICT_ID, -1);
		if(dictId != -1)
		{
			getActionBar().setSelectedNavigationItem(getPositionByTableId(dictId));
		}				
	}
	private int getPositionByTableId(long dictId)
	{
		int i = 0;
		int size = mDictsAdapter.getCount();
		for(i = 0; i < size; i++)			
			if(((Dict)mDictsAdapter.getItem(i)).dictId == dictId)
				break;
		return i;
	}
	private void onSelectDict(int position)
	{	
		long dictId = ((Dict)mDictsAdapter.getItem(position)).dictId;
		if(dictId != sp.getLong(PreferenceFields.DICT_ID, -1))
		{
			Editor ed = sp.edit();
			ed.putLong(PreferenceFields.DICT_ID, dictId);
			ed.commit();			
			reload();
		}
	}	
	private void startAddDictActivity()
	{
	    Intent intent = new Intent(this, AddDictActivity.class);
	    startActivityForResult(intent, REQUEST_CODE_ADD_DICT);		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(resultCode == RESULT_OK)
		{
			if(	requestCode == REQUEST_CODE_ADD_DICT || 
				requestCode == REQUEST_CODE_ADD_WORD ||
				requestCode == REQUEST_CODE_EDIT_WORD )
					reload();					
		
		}
	}	
	private void reload()
	{
		mWordsAdapter.setNewDictId(sp.getLong(PreferenceFields.DICT_ID, -1));
		mWordsAdapter.reloadItems();	
		mDictsAdapter.reloadItems();
	}	
	@Override
	protected void onDestroy ()
	{
		if(mCopyWordTaskId != null)
		{
			getBcConnector().unsubscribeTask(mCopyWordTaskId);
			mCopyWordTaskId = null;
		}
		if(mCutWordTaskId != null)
		{
			getBcConnector().unsubscribeTask(mCutWordTaskId);
			mCutWordTaskId = null;
		}
		if(mDeleteWordTaskId != null)
		{
			getBcConnector().unsubscribeTask(mDeleteWordTaskId);
			mDeleteWordTaskId = null;
		}	
		
		mWordsAdapter.destroy();
		mWordsAdapter = null;
		
		mIsDestroyed = true;
		super.onDestroy();
	}
	
	//=========================================================================
}
