package org.omich.lang.app.db;

import java.util.ArrayList;
import java.util.List;

import org.omich.lang.apptool.db.DbHelper;
import org.omich.lang.apptool.db.DbHelper.CursorIterator;
import org.omich.lang.apptool.events.Listeners.IListenerInt;
import org.omich.tool.bcops.ICancelledInfo;

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
		Cursor cursor = mDb.query(TNAME_WORDS, 
				new String[]{WordsCols.NATIV, WordsCols.FOREIGN, WordsCols.RATING, WordsCols.ID}, 
				null, null, null, null, null);

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
}
