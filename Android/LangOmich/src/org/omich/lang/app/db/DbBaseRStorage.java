package org.omich.lang.app.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.omich.lang.app.db.SQLiteHelper.DictsCols;
import org.omich.lang.apptool.db.DbHelper;
import org.omich.lang.apptool.db.DbHelper.CursorIterator;
import org.omich.tool.bcops.ICancelledInfo;
import org.omich.tool.events.Listeners.IListenerInt;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static org.omich.lang.app.db.SQLiteHelper.*;

abstract public class DbBaseRStorage implements IRStorage
{
	private SQLiteDatabase mDb;
	private ICancelledInfo mCi;
	private IListenerInt mPh;
	
	protected void setDb (SQLiteDatabase db){mDb = db;}
	public void setCi (ICancelledInfo ci){mCi = ci;}
	public void setPh (IListenerInt ph){mPh = ph;}
	
	public boolean isCancelled ()
	{
		return mCi == null ? false : mCi.isCancelled();
	}

	public void handleProgress (int progress)
	{
		if(mPh != null){mPh.handle(progress);}
	}
	
	@Override
	public List<Dict> getDicts ()
	{
		String where = null;
		return getDicts(where);
	}	
	@Override
	public List<Dict> getDicts (Long mobileTime)
	{
		String where = DictsCols.TIME + ">" + mobileTime;
		return getDicts(where);
	}	
	private List<Dict> getDicts (String where)
	{
		Cursor cursor = mDb.query(TNAME_DICTS, 
				new String[]{DictsCols.ID, DictsCols.NAME}, 
				where, null, null, null, null);
			
		final List<Dict> answer = new ArrayList<Dict>();
		
		DbHelper.iterateCursorAndClose(cursor, new CursorIterator()
		{
			@Override
			public void handle(Cursor cursor)
			{
				Dict dict = new Dict(cursor.getLong(0), cursor.getString(1));
				answer.add(dict);
			}
		});
		
		return answer;
	}		
	
	@Override
	public List<Word> getWordsByDictId (Long dictId)
	{
		String where = WordsCols.DICT_ID + "=" + dictId +" AND "+WordsCols.NATIV + "<> ?";
		Cursor cursor = mDb.query(TNAME_WORDS, 
				new String[]{WordsCols.NATIV, WordsCols.FOREIGN, WordsCols.RATING, WordsCols.ID}, 
				where, new String[]{""}, null, null, WordsCols.RATING);

		final List<Word> answer = new ArrayList<Word>();
		
		DbHelper.iterateCursorAndClose(cursor, new CursorIterator()
		{
			@Override
			public void handle(Cursor cursor)
			{
				Word word = new Word(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getLong(3));
				answer.add(word);
			}
		});
		
		return answer;
	}
	
	@Override
	public List<ListItem> getListItemsByDictId (Long dictId)
	{
		return insertSeparators(getWordsByDictId(dictId));
	}

	@Override
	public List<ListItem> getWordsByDictIdAndText (Long dictId, String text)
	{
		final List<Word> answer = new ArrayList<Word>();
		Cursor cursor;
	
		if(text.equals(""))
		{
			String where = WordsCols.DICT_ID + "=" + dictId +" AND "+WordsCols.NATIV + "<> ?";
			cursor = mDb.query(TNAME_WORDS, 
					new String[]{WordsCols.NATIV, WordsCols.FOREIGN, WordsCols.RATING, WordsCols.ID}, 
					where, new String[]{""}, null, null, WordsCols.RATING);
		}
		else
		{
			String where = WordsCols.DICT_ID + "=" + dictId +" AND ("
					+WordsCols.NATIV + " LIKE '%"+text+"%' OR "+WordsCols.FOREIGN + " LIKE '%"+text+"%')";
			cursor = mDb.query(TNAME_WORDS, 
					new String[]{WordsCols.NATIV, WordsCols.FOREIGN, WordsCols.RATING, WordsCols.ID}, 
					where, null, null, null, WordsCols.RATING);
		}
		DbHelper.iterateCursorAndClose(cursor, new CursorIterator()
		{
			@Override
			public void handle(Cursor cursor)
			{
				Word word = new Word(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getLong(3));
				answer.add(word);
			}
		});	
		return insertSeparators(answer);
	}
	
	@Override
	public List<ListItem> getWordsByTime (Long mobileTime)
	{
		String where = WordsCols.TIME + ">" + mobileTime;
		Cursor cursor = mDb.query(TNAME_WORDS, 
				new String[]{WordsCols.NATIV, WordsCols.FOREIGN, WordsCols.RATING, WordsCols.DICT_ID, WordsCols.TIME}, 
				where, null, null, null, WordsCols.RATING);

		final List<Word> answer = new ArrayList<Word>();
		
		DbHelper.iterateCursorAndClose(cursor, new CursorIterator()
		{
			@Override
			public void handle(Cursor cursor)
			{
				Word word = new Word(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getLong(3), cursor.getLong(4));
				answer.add(word);
			}
		});
		
		return insertSeparators(answer);
	}
	
	public List<ListItem> insertSeparators(List<Word> list)
	{
		List<ListItem> result = new ArrayList<ListItem>();
		int prev = -1;
		for(Word w : list)
		{
			if(prev != w.rating)
			{
				prev = w.rating;
				result.add(new ListItem(prev));
			}
			result.add(new ListItem(w));
		}
		return result;
	}
	
	@Override
	public List<Word> getRandomWords(Long dictId, int numberOfWordsInGame, int weight[])
	{
		if(numberOfWordsInGame < 1)
			return new ArrayList<Word>();
		
		//n - кол-во слов в выборке.
		int NUM_OF_RATINGS = 10;// количество рейтингов
		
		
		
		//количество слов в каждом рейтинге.
		String queryNumberOfWordsPerRating = "SELECT count(*), "+WordsCols.RATING+" FROM "+TNAME_WORDS+
				" WHERE "+WordsCols.DICT_ID+" = "+dictId+" AND "+WordsCols.NATIV + "<> ''"+
				" GROUP BY "+WordsCols.RATING;
		
		
		Cursor cursor = mDb.rawQuery(queryNumberOfWordsPerRating, null);
		int numberOfWordsInDict= 0;
		int numberOfWordsPerRating[] = new int[NUM_OF_RATINGS];
		while(cursor.moveToNext())
		{
			int rating = cursor.getInt(1);
			numberOfWordsPerRating[rating] = cursor.getInt(0);
			numberOfWordsInDict = numberOfWordsInDict + numberOfWordsPerRating[rating];
		}
		cursor.close();		
		
		Random random = new Random();

		//Если слов в словаре меньше, чем мы просили для игры, то просто возвращаем все слова.
		if(numberOfWordsInDict < numberOfWordsInGame)
		{
			return shuffleWords( getWordsByDictId(dictId) );
		}

		
		
		
		
		// заполнение таблицы: сколько слов каждого рейтинга должно быть в выборке
		int wordsInGamePerRating[] = new int[NUM_OF_RATINGS];
		for(int i = 0; i < numberOfWordsInGame; i++)
		{
			int accumulativeWeightsOfRatingsForCurrentWord[] = new int[NUM_OF_RATINGS];	

			//Здесь мы определяем, из каких рейтингов можно выбирать очередное слово.
			int accumulativeWeight = 0;
			for(int j = 0; j < NUM_OF_RATINGS; j++)
			{
				if(numberOfWordsPerRating[j] - wordsInGamePerRating[j] > 0)
				{
					accumulativeWeight = accumulativeWeight + weight[j];
					accumulativeWeightsOfRatingsForCurrentWord[j] = accumulativeWeight;
				}
			}
			
			//Здесь мы определяем из какого рейтинга будет очередное слово.
			int k = random.nextInt(accumulativeWeight);
			int rating = 0;
			for(int j = NUM_OF_RATINGS-1; j >= 0; j--)
			{
				if(k < accumulativeWeightsOfRatingsForCurrentWord[j])
				{
					rating = j;
				}
			}
			wordsInGamePerRating[rating]++;			
		}
		
		
		
		
		
		
		
		
		// заполнение списка: номера слов для выборки
		int selectedWordsIndices[] = new int[numberOfWordsInGame];
		int numberOfSelectedWords = 0;
		int numberOfWordsForAllPreviousRatings = 0;
		for(int currentRating = 0; currentRating < NUM_OF_RATINGS; currentRating++)
		{
			if(wordsInGamePerRating[currentRating] > 0)
			{
				if(wordsInGamePerRating[currentRating] < numberOfWordsPerRating[currentRating])
				{
					//Определяем, какие слова из рейтинга будем брать для игры.
					boolean choosenWordsOfCurrentRating[] = new boolean[numberOfWordsPerRating[currentRating]];
					for(int j = 0; j < wordsInGamePerRating[currentRating]; j++)
					{
						int r = random.nextInt(numberOfWordsPerRating[currentRating] - j);
						int ind = -1;
						for(int q = 0; q < numberOfWordsPerRating[currentRating]; q++)
						{
							if(!choosenWordsOfCurrentRating[q])
							{
								ind++;
							}
							if(ind == r)
							{
								choosenWordsOfCurrentRating[q] = true;
								break;
							}
						}
					}
					
					
					int wordsInCurrentRating = numberOfWordsPerRating[currentRating];
					int wordsInGameForCurrentRating = wordsInGamePerRating[currentRating];
					for(
						int j = 0, l = 0;
						j < wordsInCurrentRating && l < wordsInGameForCurrentRating;
						j++)
					{
						if(choosenWordsOfCurrentRating[j])
						{
							selectedWordsIndices[numberOfSelectedWords] = numberOfWordsForAllPreviousRatings+j;
							numberOfSelectedWords++;
							l++;
						}
					}
				}
				else if(wordsInGamePerRating[currentRating] == numberOfWordsPerRating[currentRating])
				{
					for(int j = 0; j < numberOfWordsPerRating[currentRating]; j++)
					{
						selectedWordsIndices[numberOfSelectedWords] = numberOfWordsForAllPreviousRatings+j;					
						numberOfSelectedWords++;
					}
				}
				//else {} этого случая быть не может,
				//потому что на этапе определения количества слов, контролировалось,
				//что мы не можем не можем взять слов больше, чем есть в рейтинге. 
			}
			numberOfWordsForAllPreviousRatings += numberOfWordsPerRating[currentRating];
		}


		//Выбираем слова с нужными номерами.
		String query = "SELECT "+ WordsCols.NATIV+", "+WordsCols.FOREIGN+", "+WordsCols.RATING+", "+WordsCols.ID+
				" FROM "+TNAME_WORDS+" WHERE "+WordsCols.DICT_ID+" = "+dictId+" AND "+WordsCols.NATIV + "<> ''"+
				" ORDER BY "+WordsCols.RATING;
		cursor = mDb.rawQuery(query, null);
		
		List<Word> answer = new ArrayList<Word>();
		int indexOfWord = 0;
		int positionInIdeciesArray = 0;
		while(cursor.moveToNext() && positionInIdeciesArray < numberOfWordsInGame)
		{	
			if(indexOfWord == selectedWordsIndices[positionInIdeciesArray])
			{
				Word word = new Word(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getLong(3));
				answer.add(word);
				positionInIdeciesArray++;
			}
			indexOfWord++;
		}
		return shuffleWords(answer);
	}
	
	private List<Word> shuffleWords(List<Word> words)
	{
		List<Word> answer = new ArrayList<Word>(words.size());
		Random random = new Random();
		for(int i = words.size(); i > 0; --i)
		{
			int r = random.nextInt(i);
			answer.add(words.remove(r));
		}
		return answer;
	}
}