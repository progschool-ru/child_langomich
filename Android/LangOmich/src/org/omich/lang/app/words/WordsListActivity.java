package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.PreferenceFields;
import org.omich.lang.app.db.Word;
import org.omich.lang.app.words.WordsListAdapter;
import org.omich.lang.apptool.activity.ABActivity;
import org.omich.tool.events.Listeners.IListener;
import org.omich.tool.events.Listeners.IListenerInt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ViewFlipper;

public class WordsListActivity extends ABActivity implements OnSharedPreferenceChangeListener
{
	private WordsListAdapter mWordsAdapter;
	private DictSpinner dictSpinner;
	
	private Animation animationFlipInSide;
	private Animation animationFlipOutSide;
	private Animation animationFlipInMain;
	private Animation animationFlipOutMain;
	private ViewFlipper viewFlipper;
	
	private Word word;
	
	private String mCopyWordTaskId;
	private String mCutWordTaskId;
	private String mDeleteWordTaskId;
	
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_wordslist);	
		ListView lv = (ListView)findViewById(R.id.wordslist_list);
		
		sp.registerOnSharedPreferenceChangeListener(this);	
		
		mWordsAdapter = new WordsListAdapter(this, getBcConnector(), 
				sp.getLong(PreferenceFields.DICT_ID, -1));
		mWordsAdapter.reloadItems();
		
		dictSpinner = new DictSpinner((Spinner)findViewById(R.id.wordslist_spinner), this, new IListenerInt()
		{
			public void handle (int key)
			{ 
				if(key == dictSpinner.ADD_DICT)
					startAddDictActivity();
				else if(key == dictSpinner.SELECT_DICT)
					reload();
			}			
		});
		lv.setAdapter(mWordsAdapter);
		
		animationFlipInSide = AnimationUtils.loadAnimation(this, R.anim.flipin_side);
		animationFlipOutSide = AnimationUtils.loadAnimation(this, R.anim.flipout_side);
		animationFlipInMain = AnimationUtils.loadAnimation(this, R.anim.flipin_main);
		animationFlipOutMain = AnimationUtils.loadAnimation(this, R.anim.flipout_main);
		
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				if(mWordsAdapter.getSelectedPosition() != -1 && viewFlipper != null)
				{
					viewFlipper.setInAnimation(animationFlipInMain);
					viewFlipper.setOutAnimation(animationFlipOutMain);			
					viewFlipper.showPrevious();
				}

				word = (Word)mWordsAdapter.getItem(position);
				mWordsAdapter.setSelectedPosition(position);

				viewFlipper = (ViewFlipper)view.findViewById(R.id.viewflipper);
					
				int viewId = viewFlipper.getCurrentView().getId();
				if(viewId == R.id.screen_one)
				{
					viewFlipper.setInAnimation(animationFlipInSide);
					viewFlipper.setOutAnimation(animationFlipOutSide);				
					viewFlipper.showNext();	
					ImageButton but = (ImageButton)view.findViewById(R.id.item_wordslist_button_return);
					but.setOnClickListener(new OnClickListener()
					{
						public void onClick(View v)
						{
							viewFlipper.setInAnimation(animationFlipInMain);
							viewFlipper.setOutAnimation(animationFlipOutMain);			
							viewFlipper.showPrevious();
							mWordsAdapter.setSelectedPosition(-1);
						}
					});
				}	
				mWordsAdapter.notifyDataSetChanged();
			}
		});

	}
	public void onEdit (View v)
	{
		if(word == null)
			return;
		Intent intent = new Intent(this, EditWordActivity.class);
		intent.putExtra("id", word.id);
		intent.putExtra("nativ", word.nativ);
		intent.putExtra("foreign", word.foreign);
		startActivityForResult(intent, 2);
	}	
	public void onCopy (View v)
	{
		System.out.println("Copy");
	}
	public void onCut (View v)
	{
		System.out.println("Cut");
	}
	public void onDelete (View v)
	{
		if(word == null || mDeleteWordTaskId != null)
			return;

		String successText = getResources().getString(R.string.wordslist_deleted);
		
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
	private void startAddDictActivity()
	{
	    Intent intent = new Intent(this, AddDictActivity.class);
	    startActivityForResult(intent, 1);		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(requestCode == 1 && resultCode == RESULT_OK && data != null)
		{
			if(data.getBooleanExtra("result", true))
			{
				reload();			
			}
			
		}
		else if(requestCode == 2 && resultCode == RESULT_OK && data != null)
		{
			if(data.getBooleanExtra("result", true))
			{
				mWordsAdapter.reloadItems();			
			}
			
		}		
	}	
	private void reload()
	{
		mWordsAdapter.setNewDictId(sp.getLong(PreferenceFields.DICT_ID, -1));
		mWordsAdapter.reloadItems();					
		dictSpinner.reload();			
	}
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
	{
		if(key.equals(PreferenceFields.IS_TIMING))
		{
			  if(prefs.getBoolean(PreferenceFields.IS_TIMING, false))
				  itemTiming.setIcon(R.drawable.ic_sunc_enable);
			  else
				  itemTiming.setIcon(R.drawable.ic_sunc_disable);			
		}	
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

		dictSpinner.destroy();
		
		super.onDestroy();
	}
	
	//=========================================================================
}
