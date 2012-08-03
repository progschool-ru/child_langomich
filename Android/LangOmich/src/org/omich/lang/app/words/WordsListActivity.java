package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.PreferenceFields;
import org.omich.lang.app.words.WordsListAdapter;
import org.omich.lang.apptool.activity.ABActivity;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ViewFlipper;

public class WordsListActivity extends ABActivity implements OnSharedPreferenceChangeListener
{
	private WordsListAdapter mWordsAdapter;
	private DictSpinner dictSpinner;
	Animation anim;
	View viewSel;
	
	private Animation animationFlipInSide;
	private Animation animationFlipOutSide;
	private Animation animationFlipInMain;
	private Animation animationFlipOutMain;
	ViewFlipper MyViewFlipper;
	
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
				if(mWordsAdapter.getSelectedPosition() != -1 && MyViewFlipper != null)
				{
					MyViewFlipper.setInAnimation(animationFlipInMain);
					MyViewFlipper.setOutAnimation(animationFlipOutMain);			
					MyViewFlipper.showPrevious();
				}
//				Word word = (Word)mWordsAdapter.getItem(position);
				mWordsAdapter.setSelectedPosition(position);

				MyViewFlipper = (ViewFlipper)view.findViewById(R.id.viewflipper);
					
				int viewId = MyViewFlipper.getCurrentView().getId();
				if(viewId == R.id.screen_one)
				{
					MyViewFlipper.setInAnimation(animationFlipInSide);
					MyViewFlipper.setOutAnimation(animationFlipOutSide);				
					MyViewFlipper.showNext();	
					Button but = (Button)view.findViewById(R.id.item_wordslist_button_return);
					but.setOnClickListener(new OnClickListener()
					{
						public void onClick(View v)
						{
							MyViewFlipper.setInAnimation(animationFlipInMain);
							MyViewFlipper.setOutAnimation(animationFlipOutMain);			
							MyViewFlipper.showPrevious();
							mWordsAdapter.setSelectedPosition(-1);
						}
					});
				}	
				mWordsAdapter.notifyDataSetChanged();
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
		mWordsAdapter.destroy();
		mWordsAdapter = null;

		dictSpinner.destroy();
		
		super.onDestroy();
	}
	
	//=========================================================================
}
