package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.Word;
import org.omich.lang.apptool.activity.BcActivity;
import org.omich.lang.apptool.events.Listeners.IListener;
import org.omich.tool.bcops.BcEventHelper;
import org.omich.tool.bcops.BcService;
import org.omich.tool.bcops.IBcConnector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class GameActivity extends BcActivity
{
	@SuppressWarnings("unused")
	private boolean mIsDestroyed;
	private TextView tvn;
	private TextView tvf;
	private Button bt1;
	private Button bt2;
	final int GAME = 1;
	final int I_KNOW = 2;
	final int I_DONT_KNOW = 3;
	private int currentScreen = GAME;
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
		tvn.setText("");
		tvf.setText("");
		bt1.setText("Знаю");
		bt2.setText("Не знаю");
		OnClickListener l =  new OnClickListener(){
			public void onClick(View v) 
			{
				switch (v.getId())
				{
					case R.id.button_1:
						if(currentScreen == GAME)
						{
							tvf.setText(word.foreign);
							bt1.setText("Верно");
							bt2.setText("Я ошибся");
							currentScreen = I_KNOW;
						}
						else if(currentScreen == I_KNOW) // повышение рейтинга
						{
							ratingUpgrade();
							tvn.setText("");
							tvf.setText("");
							getWord();
							bt1.setText("Знаю");
							bt2.setText("Не знаю");
							currentScreen = GAME;	
						}
						else if(currentScreen == I_DONT_KNOW) // понижение рейтинга
						{
							ratingDowngrade();
							tvn.setText("");
							tvf.setText("");
							getWord();
							bt1.setText("Знаю");
							bt2.setText("Не знаю");
							bt2.setVisibility(v.VISIBLE);
							currentScreen = GAME;
						}
						break;
				    case R.id.button_2:
						if(currentScreen == GAME)
						{
							tvf.setText(word.foreign);
							bt1.setText("Продолжить");
							bt2.setVisibility(v.INVISIBLE);
							currentScreen = I_DONT_KNOW;
						}
						else if(currentScreen == I_KNOW)// понижение рейтинга
						{
							ratingDowngrade();
							tvn.setText("");
							tvf.setText("");
							getWord();
							bt1.setText("Знаю");
							bt2.setText("Не знаю");
							currentScreen = GAME;	
						}
				    	break;
				}
			}
		};
		bt1.setOnClickListener(l);
		bt2.setOnClickListener(l);
		getWord();
	}
	@Override
	protected void onDestroy ()
	{
//		gameAdapter.destroy();
//		gameAdapter = null;
		
		mIsDestroyed = true;
		super.onDestroy();
	}
	public void ratingUpgrade()
	{
		int rating;
		if(word.rating<10)
			rating = word.rating + 1;
		else
			rating = 10;
		setNewRating(word.id, rating);
	}
	public void ratingDowngrade()
	{
		int rating;
		if(word.rating>2)
			rating = word.rating - 2;
		else
			rating = 0;
		setNewRating(word.id, rating);
	}
	Word word;
	private String mGetWordTaskId;
	public void getWord ()
	{
		if(mGetWordTaskId != null)
			return;

		IBcConnector conn = getBcConnector();
		conn.startTask(BcService.class, GetWordTask.class, 
				new Intent(), new IListener<Intent>()
				{
					public void handle(Intent value)
					{
						BcEventHelper.parseEvent(value, null, null, new IListener<Bundle>()
						{
							public void handle (Bundle b)
							{
								mGetWordTaskId = null;
			
								word = b.getParcelable(BundleFields.WORD);	
								tvn.setText(word.nativ);
							}
						}, null, null);
					}
				});	
	}	
	private String mSetRatingTaskId;
	public void setNewRating (long id, int rating)
	{
		if(mSetRatingTaskId != null)
			return;

		Intent intent = SetNewRatingTask.createIntent(id,rating);
		mSetRatingTaskId = getBcConnector().startTask(BcService.class, SetNewRatingTask.class, intent,
				new IListener<Intent>()
				{
					public void handle (Intent intent)
					{
						if(mIsDestroyed)
							return;

						BcEventHelper.parseEvent(intent, null, null, new IListener<Bundle>()
						{
							public void handle (Bundle bundle){mSetRatingTaskId = null;}
						}, null, null);
					}
				});
	}	
	//=========================================================================
}
