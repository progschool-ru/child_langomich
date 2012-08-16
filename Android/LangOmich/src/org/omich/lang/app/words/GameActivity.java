package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.PreferenceFields;
import org.omich.lang.app.SettingsActivity;
import org.omich.lang.app.db.Dict;
import org.omich.lang.apptool.activity.BcActivity;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends BcActivity
{
	private static final int GAME = 1;
	private static final int I_KNOW = 2;
	private static final int I_DONT_KNOW = 3;
	
	private TextView tvn;
	private TextView tvf;
	private Button bt1;
	private Button bt2;
	private int currentScreen = GAME;

	private Game game;
	private SharedPreferences sp;
	private DictsListAdapter mDictsAdapter;
	
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_game);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		mDictsAdapter = new DictsListAdapter(this, getBcConnector(), false, new IListenerVoid()
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
				onSelectDict(position);
				return true;
			}
		});

		tvn = (TextView)findViewById(R.id.item_wordslist_text_nativ);
		tvf = (TextView)findViewById(R.id.item_wordslist_text_foreign);
		bt1 = (Button)findViewById(R.id.button_1);
		bt2 = (Button)findViewById(R.id.button_2);
		
		game = new Game(this, sp.getLong(PreferenceFields.DICT_ID, -1), new IListenerVoid()
		{
			public void handle ()
			{ 
				tvn.setText(game.getText(game.NATIV));
				bt1.setVisibility(View.VISIBLE);
				bt2.setVisibility(View.VISIBLE);			
			}			
		});
		
		OnClickListener l =  new OnClickListener(){
			public void onClick(View v) 
			{
				switch (v.getId())
				{
					case R.id.button_1:				
						switch (currentScreen)
						{
							case GAME:
								setScreen(I_KNOW);
								break;
							case I_KNOW:
								game.changeTheRating(game.UPGRADE);
								setScreen(GAME);
								break;
						}
						break;
				    case R.id.button_2:
						switch (currentScreen)
						{
							case GAME:
								setScreen(I_DONT_KNOW);
								break;
							case I_KNOW:
								game.changeTheRating(game.DOWNGRADE);
								setScreen(GAME);	
								break;
							case I_DONT_KNOW:
								game.changeTheRating(game.DOWNGRADE);
								setScreen(GAME);
								break;								
						}
						break;
				}
			}
		};
		
		bt1.setOnClickListener(l);
		bt2.setOnClickListener(l);
		
		setScreen(GAME);
	}
	@Override
	protected void onResume()
	{		
		setSelectedPosition();
		game.setNewDict(sp.getLong(PreferenceFields.DICT_ID, -1));	
		setScreen(GAME);
		super.onResume();
	}
	@Override
	protected void onDestroy ()
	{
		game.destroy();
		super.onDestroy();
	}
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		  MenuInflater inflater = getMenuInflater();
		  inflater.inflate(R.menu.menu_game, menu);	

		  return super.onCreateOptionsMenu(menu);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		if(id == R.id.app_menu_item_button_dictionary)
		{
			getForResultStarter().startForResult(new Intent(this, WordsListActivity.class), null);
		}
		else if(id == R.id.app_menu_item_button_settings)
		{
			getForResultStarter().startForResult(new Intent(this, SettingsActivity.class), null);
		}
		return true;
    }	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		mDictsAdapter.reloadItems();
	}
	//=========================================================================
	private void setScreen(int screen)
	{
		if(screen == GAME)
		{
			tvn.setText("");
			tvf.setText("");		
			bt1.setText("\n"+getString(R.string.training_text_i_know)+"\n");
			bt2.setText("\n"+getString(R.string.training_text_i_dont_know)+"\n");
			bt1.setVisibility(View.INVISIBLE);
			bt2.setVisibility(View.INVISIBLE);
			game.getNextWord();
			currentScreen = GAME;		
		}
		else if(screen == I_KNOW)
		{
			tvf.setText(game.getText(game.FOREIGN));
			bt1.setText("\n"+getString(R.string.training_text_is_true)+"\n");
			bt2.setText("\n"+getString(R.string.training_text_is_false)+"\n");
			currentScreen = I_KNOW;	
		}
		else if(screen == I_DONT_KNOW)
		{
			tvf.setText(game.getText(game.FOREIGN));
			bt1.setVisibility(View.INVISIBLE);
			bt2.setText("\n"+getString(R.string.training_text_is_next)+"\n");
			currentScreen = I_DONT_KNOW;
		}		
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
			game.setNewDict(dictId);
			setScreen(GAME);
		}
	}
}
