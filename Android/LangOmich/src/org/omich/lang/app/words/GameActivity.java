package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.PreferenceFields;
import org.omich.lang.apptool.activity.ABActivity;
import org.omich.tool.events.Listeners.IListenerVoid;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends ABActivity implements OnSharedPreferenceChangeListener
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
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_game);
		sp.registerOnSharedPreferenceChangeListener(this);	
		
		tvn = (TextView)findViewById(R.id.item_wordslist_text_nativ);
		tvf = (TextView)findViewById(R.id.item_wordslist_text_foreign);
		bt1 = (Button)findViewById(R.id.button_1);
		bt2 = (Button)findViewById(R.id.button_2);
		
		game = new Game(this,sp.getLong(PreferenceFields.DICT_ID, -1), sp.getInt(PreferenceFields.IDEAL_NUMBER, 5),
				getWeight(), new IListenerVoid()
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
		game.setNewSettings(sp.getLong(PreferenceFields.DICT_ID, -1), sp.getInt(PreferenceFields.IDEAL_NUMBER, 5),
				getWeight());	
		super.onResume();
	}
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
	{
		if(key.equals("isTiming"))
		{
			if(prefs.getBoolean("isTiming", false))
				itemTiming.setIcon(R.drawable.ic_sunc_enable);
			else
				itemTiming.setIcon(R.drawable.ic_sunc_disable);			
		}
	}	
	@Override
	protected void onDestroy ()
	{
		game.destroy();
		super.onDestroy();
	}
	
	//=========================================================================
	private void setScreen(int screen)
	{
		if(screen == GAME)
		{
			tvn.setText("");
			tvf.setText("");		
			bt1.setText(getString(R.string.i_know));
			bt2.setText(getString(R.string.i_dont_know));
			bt1.setVisibility(View.INVISIBLE);
			bt2.setVisibility(View.INVISIBLE);
			game.getNextWord();
			currentScreen = GAME;		
		}
		else if(screen == I_KNOW)
		{
			tvf.setText(game.getText(game.FOREIGN));
			bt1.setText(getString(R.string.isTrue));
			bt2.setText(getString(R.string.isFalse));
			currentScreen = I_KNOW;	
		}
		else if(screen == I_DONT_KNOW)
		{
			tvf.setText(game.getText(game.FOREIGN));
			bt1.setVisibility(View.INVISIBLE);
			bt2.setText(getString(R.string.next));
			currentScreen = I_DONT_KNOW;
		}		
	}
	private int [] getWeight()
	{
		int weight[] = new int[10];
		
		weight [0] = sp.getInt(PreferenceFields.WEIGHT_ZERO, 100);
		weight [1] = sp.getInt(PreferenceFields.WEIGHT_ONE, 80);
		weight [2] = sp.getInt(PreferenceFields.WEIGHT_TWO, 60);
		weight [3] = sp.getInt(PreferenceFields.WEIGHT_THREE, 40);
		weight [4] = sp.getInt(PreferenceFields.WEIGHT_FOUR, 30);
		weight [5] = sp.getInt(PreferenceFields.WEIGHT_FIVE, 20);
		weight [6] = sp.getInt(PreferenceFields.WEIGHT_SIX, 10);
		weight [7] = sp.getInt(PreferenceFields.WEIGHT_SEVEN, 5);
		weight [8] = sp.getInt(PreferenceFields.WEIGHT_EIGHT, 3);
		weight [9] = sp.getInt(PreferenceFields.WEIGHT_NINE, 1);	
		
		return weight;
	}
}
