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
		Cursor cursor = mDb.query(TNAME_DICTS, 
				new String[]{DictsCols.ID, DictsCols.NAME}, 
				null, null, null, null, null);

		final List<Dict> answer = new ArrayList<Dict>();
		
		DbHelper.iterateCursorAndClose(cursor, new CursorIterator()
		{
			public void handle(Cursor cursor)
			{
				Dict dict = new Dict(cursor.getLong(0), cursor.getString(1));
				answer.add(dict);
			}
		});
		
		return answer;
	}
	
	public List<Word> getWords ()
	{
		return getWords(null);
	}
	public List<Word> getWords (String where)
	{
		Cursor cursor = mDb.query(TNAME_WORDS, 
				new String[]{WordsCols.NATIV, WordsCols.FOREIGN, WordsCols.RATING, WordsCols.ID}, 
				where, null, null, null, null);

		final List<Word> answer = new ArrayList<Word>();
		
		DbHelper.iterateCursorAndClose(cursor, new CursorIterator()
		{
			public void handle(Cursor cursor)
			{
				Word word = new Word(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getLong(3));
				answer.add(word);
			}
		});
		
		return answer;
	}
	public Word getRandomWord ()
	{		
		int r = 0;
		String query = "SELECT count(*), "+WordsCols.RATING+" FROM "+TNAME_WORDS+" GROUP BY "+WordsCols.RATING; 
		Cursor cursor = mDb.rawQuery(query, null);
		int size[] = new int[10];
		int max[] = new int[10];	
		for(int i = 0; i < 10;i++) { max[i] = 0;size[i] = 0;}
		while(cursor.moveToNext())
		{
			int i = cursor.getInt(1);
			size[i] = cursor.getInt(0);
			r = r + 10 - i;
			max[i] = r;
		}
		int random = new Random().nextInt(r);
		for(int i = 9; i >= 0;i--)
			if(random < max[i])
				r = i;
		random = new Random().nextInt(size[r]);
		String where = WordsCols.RATING + "=" + r;		
		return (Word)getWords(where).get(random);
	}	
}
