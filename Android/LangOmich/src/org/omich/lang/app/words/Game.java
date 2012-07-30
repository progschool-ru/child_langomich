package org.omich.lang.app.words;

import java.util.ArrayList;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
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
	private int idealNumber = 3;
	private int realNumber = 0;
	private int currentNumber = 0;
	private Word word;
	private int[] weight = {100, 80, 60, 40, 20, 10, 5, 3, 2, 1};
	private ArrayList<Word> words;
	
	private String dictIsEmptyNativ;
	private String dictIsEmptyEnglish;
	
	private String mSetRatingTaskId;
	private String mGetRandomWordsTaskId;	
	private boolean mIsDestroyed;
	
	public final int UPGRADE = 1;
	public final int DOWNGRADE = 2;
	
	public final int NATIV = 1;
	public final int FOREIGN = 2;	
	
	private IListenerVoid lv;
			
	public Game(Context context, long dictId, int idealNumber, int []weight, IListenerVoid lv)
	{
		this.lv = lv;
		mBcConnector = new BcConnector(context);
		dictIsEmptyNativ = context.getResources().getString(R.string.game_die_nativ);
		dictIsEmptyEnglish = context.getResources().getString(R.string.game_die_english);
		setNewSettings(dictId, idealNumber, weight);
	}
	public void setNewSettings(long dictId, int idealNumber, int []weight)
	{
		this.dictId = dictId;
		this.idealNumber = idealNumber;
		this.weight = weight;
		getRandomWords();
	}
	public String getText(int key)
	{
		String text = "";
		if(word != null)
			switch (key)
			{
				case NATIV:				
					text = word.nativ;
					break;
				case FOREIGN:
					text = word.foreign;
					break;
			}
		return text;
	}
	public void changeTheRating(int key)
	{
		int rating = 0;
		switch (key)
		{
			case UPGRADE:				
				if(word.rating<9)
					rating = word.rating + 1;
				else
					rating = 9;
				break;
		    case DOWNGRADE:
				if(word.rating>2)
					rating = word.rating - 2;
				else
					rating = 0;
				break;
		}
		setNewRating(word.id, rating);
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
		
		Intent intent = GetRandomWordsTask.createIntent(dictId, idealNumber, weight);
		mGetRandomWordsTaskId = mBcConnector.startTypicalTask(GetRandomWordsTask.class, intent, new IListener<Bundle>()
						{
							public void handle (Bundle b)
							{
								if(mIsDestroyed)
									return;
								
								mGetRandomWordsTaskId = null;
			
								words = b.<Word>getParcelableArrayList(BundleFields.WORDS_LIST);	
								if(words == null)
									realNumber = 0;
								else
									realNumber = words.size();
								currentNumber = 0;
								
								updateWord();
							}
						});
	}	
	private void updateWord()
	{
		if(realNumber > 0)
		{
			word = words.get(currentNumber);
			currentNumber++;
		}
		else
			word = new Word(dictIsEmptyNativ, dictIsEmptyEnglish, 0, -1); 
		lv.handle();
	}	
	private void setNewRating(long id, int rating)
	{
		if(mSetRatingTaskId != null)
			return;

		Intent intent = SetNewRatingTask.createIntent(id,rating);
		mSetRatingTaskId = mBcConnector.startTypicalTask(SetNewRatingTask.class, 
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
