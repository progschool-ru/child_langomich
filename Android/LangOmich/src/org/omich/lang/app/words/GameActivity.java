package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.Word;
import org.omich.lang.apptool.activity.BcActivity;
import org.omich.tool.bcops.IBcConnector;
import org.omich.tool.events.Listeners.IListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class GameActivity extends BcActivity
{
	private static final int GAME = 1;
	private static final int I_KNOW = 2;
	private static final int I_DONT_KNOW = 3;
	
	private boolean mIsDestroyed;
	private TextView tvn;
	private TextView tvf;
	private Button bt1;
	private Button bt2;
	private int currentScreen = GAME;
	
	private Word word;
	private String mGetWordTaskId;
	private String mSetRatingTaskId;

	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_game);
		tvn = (TextView)findViewById(R.id.item_wordslist_text_nativ);
		tvf = (TextView)findViewById(R.id.item_wordslist_text_foreign);
		bt1 = (Button)findViewById(R.id.button_1);
		bt2 = (Button)findViewById(R.id.button_2);
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
								upgradeRating();
								setScreen(GAME);
								break;
							case I_DONT_KNOW:
								downgradeRating();
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
								downgradeRating();
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
	protected void onDestroy ()
	{
		if(mGetWordTaskId != null)
		{
			getBcConnector().unsubscribeTask(mGetWordTaskId);
			mGetWordTaskId = null;
		}
		
		if(mSetRatingTaskId != null)
		{
			getBcConnector().unsubscribeTask(mSetRatingTaskId);
			mSetRatingTaskId = null;
		}
		
		mIsDestroyed = true;
		super.onDestroy();
	}
	
	//=========================================================================
	private void setScreen(int screen)
	{
		if(screen == GAME)
		{
			tvn.setText("");
			tvf.setText("");
			getWord();
			bt1.setText(getString(R.string.i_know));
			bt2.setText(getString(R.string.i_dont_know));
			bt2.setVisibility(View.VISIBLE);
			currentScreen = GAME;		
		}
		else if(screen == I_KNOW)
		{
			tvf.setText(word.foreign);
			bt1.setText(getString(R.string.isTrue));
			bt2.setText(getString(R.string.isFalse));
			currentScreen = I_KNOW;	
		}
		else if(screen == I_DONT_KNOW)
		{
			tvf.setText(word.foreign);
			bt1.setText(getString(R.string.next));
			bt2.setVisibility(View.INVISIBLE);
			currentScreen = I_DONT_KNOW;
		}		
	}
	private void upgradeRating()
	{
		int rating;
		if(word.rating<9)
			rating = word.rating + 1;
		else
			rating = 9;
		setNewRating(word.id, rating);
	}

	private void downgradeRating()
	{
		int rating;
		if(word.rating>2)
			rating = word.rating - 2;
		else
			rating = 0;
		setNewRating(word.id, rating);
	}

	private void getWord ()
	{
		if(mGetWordTaskId != null)
			return;

		IBcConnector conn = getBcConnector();
		conn.startTypicalTask(GetRandomWordTask.class, new Intent(), new IListener<Bundle>()
						{
							public void handle (Bundle b)
							{
								if(mIsDestroyed)
									return;

								mGetWordTaskId = null;
			
								word = b.getParcelable(BundleFields.WORD);	
								tvn.setText(word.nativ);
							}
						});
	}	

	private void setNewRating (long id, int rating)
	{
		if(mSetRatingTaskId != null)
			return;

		Intent intent = SetNewRatingTask.createIntent(id,rating);
		mSetRatingTaskId = getBcConnector().startTypicalTask(SetNewRatingTask.class, 
				intent, 
				new IListener<Bundle>()
				{
					public void handle (Bundle bundle)
					{
						if(mIsDestroyed)
							return;
				
						mSetRatingTaskId = null;
					}
				});
	}
}
