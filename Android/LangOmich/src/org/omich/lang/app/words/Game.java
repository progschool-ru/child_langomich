package org.omich.lang.app.words;

import java.util.ArrayList;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.ListItem;
import org.omich.lang.app.db.Word;
import org.omich.tool.bcops.BcConnector;
import org.omich.tool.events.Listeners.IListener;
import org.omich.tool.events.Listeners.IListenerVoid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Game 
{
	private BcConnector mBcConnector;

	private long dictId = -1;
	private int idealNumber = 30;
	private int realNumber = 0;
	private int currentNumber = 0;
	private Word word;
	private int[] weight = {512, 256, 128, 64, 32, 16, 8, 4, 2, 1};
	private ArrayList<Word> words;
	
	private String dictIsEmptyNativ;
	
	private String mSetRatingTaskId;
	private String mGetRandomWordsTaskId;	
	private boolean mIsDestroyed;
	
	public final int UPGRADE = 1;
	public final int DOWNGRADE = 2;
	
	public final int NATIV = 1;
	public final int FOREIGN = 2;	
	
	private IListenerVoid lv;
	private boolean isUpdate = false;
	
	public Game(Context context, long dictId, IListenerVoid lv)
	{
		this.lv = lv;

		mBcConnector = new BcConnector(context);
		dictIsEmptyNativ = context.getResources().getString(R.string.training_text_die_nativ);
		setNewDict(dictId);
	}
	public void setNewDict(long dictId)
	{
		this.dictId = dictId;
		getRandomWords();
	}
	public String getText(int key)
	{
		String text = "";
		if(word != null)
		{
			switch (key)
			{
				case NATIV:				
					text = word.nativ;
					break;
				case FOREIGN:
					text = word.foreign;
					break;
			}
		}
		return text;
	}
	public void changeTheRating(int key)
	{
		if(word != null)
		{
			switch (key)
			{
				case UPGRADE:				
					if(word.rating<9)
					{
						setNewRating(word.id, word.rating + 1);
					}
					break;
			    case DOWNGRADE:
					if(word.rating > 1)
					{
						setNewRating(word.id,word.rating - 1);
					}
					break;
			}	
		}
	}
	public void getNextWord()
	{
		if(words == null || currentNumber == realNumber)
			getRandomWords();
		else
			updateWord();
	}	
	private void getRandomWords()
	{
		if(mGetRandomWordsTaskId != null)
			return;		
		isUpdate = true;
		
		Intent intent = GetRandomWordsTask.createIntent(dictId, idealNumber, weight);
		mGetRandomWordsTaskId = mBcConnector.startTypicalTask(GetRandomWordsTask.class, intent, new IListener<Bundle>()
						{
							@Override
							public void handle (Bundle b)
							{
								if(mIsDestroyed)
									return;
								
								mGetRandomWordsTaskId = null;
								words = new ArrayList<Word>();
								words = b.<Word>getParcelableArrayList(BundleFields.WORDS_LIST);	
								if(words == null)
									realNumber = 0;
								else
									realNumber = words.size();
								currentNumber = 0;
								
								isUpdate = false;
								updateWord();
							}
						});
	}	
	
	private void updateWord()
	{
		if(!isUpdate)
		{
			if(realNumber > 0)
			{							
				currentNumber++;
				currentNumber %= words.size();
				word = words.get(currentNumber);				
			}
			else if(realNumber == 0)
				getRandomWords();
			else
			{
				currentNumber++;
				currentNumber %= words.size();
				updateWord();
			}
			lv.handle();
		}	
	}	
	
	private void setNewRating(long id, int rating)
	{
		if(mSetRatingTaskId != null)
			return;

		Intent intent = SetNewRatingTask.createIntent(id, rating);
		mSetRatingTaskId = mBcConnector.startTypicalTask(SetNewRatingTask.class, 
				intent, 
				new IListener<Bundle>()
				{
					@Override
					public void handle (Bundle bundle)
					{
						if(mIsDestroyed)
							return;
				
						mSetRatingTaskId = null;
					}
				});
	}
	public void destroy()
	{		
		if(mGetRandomWordsTaskId != null)
		{
			mBcConnector.unsubscribeTask(mGetRandomWordsTaskId);
			mGetRandomWordsTaskId = null;
		}
		if(mSetRatingTaskId != null)
		{
			mBcConnector.unsubscribeTask(mSetRatingTaskId);
			mSetRatingTaskId = null;
		}	
		mIsDestroyed = true;
	}
}
