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
	
	public List<Dict> getDicts ()
	{
		String where = null;
		return getDicts(where);
	}	
	public List<Dict> getDicts (Long mobileTime)
	{
		String where = DictsCols.TIME + ">" + mobileTime;
		return getDicts(where);
	}	
	private List<Dict> getDicts (String where)
	{
		Cursor cursor = mDb.query(TNAME_DICTS, 
				new String[]{DictsCols.ID, DictsCols.SERVER_ID, DictsCols.NAME, DictsCols.TIME}, 
				where, null, null, null, null);
			
		final List<Dict> answer = new ArrayList<Dict>();
		
		DbHelper.iterateCursorAndClose(cursor, new CursorIterator()
		{
			public void handle(Cursor cursor)
			{
				Dict dict = new Dict(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3));
				answer.add(dict);
			}
		});
		
		return answer;
	}		
	
	public List<ListItem> getWordsByDictId (Long dictId)
	{
		String where = WordsCols.DICT_ID + "=" + dictId +" AND "+WordsCols.NATIV + "<> ?";
		Cursor cursor = mDb.query(TNAME_WORDS, 
				new String[]{WordsCols.NATIV, WordsCols.FOREIGN, WordsCols.RATING, WordsCols.ID}, 
				where, new String[]{""}, null, null, WordsCols.RATING);

		final List<Word> answer = new ArrayList<Word>();
		
		DbHelper.iterateCursorAndClose(cursor, new CursorIterator()
		{
			public void handle(Cursor cursor)
			{
				Word word = new Word(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getLong(3));
				answer.add(word);
			}
		});
		
		return insertSeparators(answer);
	}
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
			public void handle(Cursor cursor)
			{
				Word word = new Word(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getLong(3));
				answer.add(word);
			}
		});	
		return insertSeparators(answer);
	}
	
	public List<ListItem> getWordsByTime (Long mobileTime)
	{
		String where = WordsCols.TIME + ">" + mobileTime;
		Cursor cursor = mDb.query(TNAME_WORDS, 
				new String[]{WordsCols.NATIV, WordsCols.FOREIGN, WordsCols.RATING, WordsCols.DICT_ID, WordsCols.TIME}, 
				where, null, null, null, WordsCols.RATING);

		final List<Word> answer = new ArrayList<Word>();
		
		DbHelper.iterateCursorAndClose(cursor, new CursorIterator()
		{
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
	
	public List<ListItem> getRandomWords(Long dictId, int n, int weight[])
	{
		int N = 10;// количество рейтингов
		List<ListItem> answer = new ArrayList<ListItem>();
		if(n < 1)
			return answer;
		String query = "SELECT count(*), "+WordsCols.RATING+" FROM "+TNAME_WORDS+
				" WHERE "+WordsCols.DICT_ID+" = "+dictId+" AND "+WordsCols.NATIV + "<> ?"+
				" GROUP BY "+WordsCols.RATING; 
		Cursor cursor = mDb.rawQuery(query, new String[]{""});			
		int allSize = 0;
		int size[] = new int[N];
		int table[] = new int[N];
		int max[] = new int[N];	
		for(int i = 0; i < N; i++) {max[i] = 0; size[i] = 0; table[i] = 0;}
		while(cursor.moveToNext())
		{
			int i = cursor.getInt(1);
			size[i] = cursor.getInt(0);
			allSize = allSize + size[i];
		}
		cursor.close();		
		
		Random random = new Random();
		if(allSize < n)
		{
			answer = getWordsByDictId(dictId);
			List<ListItem> answer2 = new ArrayList<ListItem>();
			for(int i = allSize; i > 0; i--)
			{
				int r = random.nextInt(i);
				ListItem word = answer.get(r);
				answer2.add(word);
				answer.remove(r);
			}
			return answer2;
		}		
		int r = 0;
		int k = 0;
		for(int i = 0; i < n; i++) // заполнение таблицы: сколько слов каждого рейтинга должно быть в выборке
		{
			r = 0;
			for(int j = 0; j < N; j++)
				if(size[j] - table[j] > 0)
				{
					r = r + weight[j];
					max[j] = r;
				}
			k = random.nextInt(r);
			for(int j = N-1; j >= 0; j--)
				if(k < max[j])
					r = j;			
			table[r]++;			
		}
		int list[] = new int[n];
		k = 0;
		allSize = 0;
		for(int i = 0; i < N; i++)// заполнение списка: номера слов для выборки
		{
			if(table[i] > 0)
			{
				if(table[i] < size[i])
				{
					int m[] = new int[size[i]];
					for(int j = 0; j < size[i]; j++) m[j] = 1;
					for(int j = 0; j < table[i]; j++)
					{
						r = random.nextInt(size[i] - j);
						int ind = -1;
						for(int q = 0; q < size[i]; q++)
						{
							if(m[q]!=0) ind++;
							if(ind == r)
							{
								m[q] = 0;
								break;
							}
						}
					}
					int l = 0;
					for(int j = 0; j < size[i]; j++)
					{
						if(m[j] == 0)
						{
							list[k] = allSize+j;
							k++;
							l++;
							if(l == table[i])
								break;
						}
					}
				}
				else if(table[i] == size[i])
					for(int j = 0; j < size[i]; j++)
					{
						list[k] = allSize+j;					
						k++;
					}	
			}
			allSize = allSize + size[i];
		}				
		query = "SELECT "+ WordsCols.NATIV+", "+WordsCols.FOREIGN+", "+WordsCols.RATING+", "+WordsCols.ID+
				" FROM "+TNAME_WORDS+" WHERE "+WordsCols.DICT_ID+" = "+dictId+" AND "+WordsCols.NATIV + "<> ?"+
				" ORDER BY "+WordsCols.RATING;
		cursor = mDb.rawQuery(query, new String[]{""});
		
		k = 0;
		r = 0;
		while(cursor.moveToNext())
		{	
			if(k == list[r])
			{
				Word word = new Word(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getLong(3));
				answer.add(new ListItem(word));
				r++;
			}
			k++;
			if(r  == n)
				break;
		}
		List<ListItem> answer2 = new ArrayList<ListItem>();
		for(int i = n; i > 0; i--)
		{
			r = random.nextInt(i);
			ListItem word = answer.get(r);
			answer2.add(word);
			answer.remove(r);
		}			
		return answer2;
	}
}
