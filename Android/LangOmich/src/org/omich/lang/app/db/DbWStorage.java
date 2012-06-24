package org.omich.lang.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import static org.omich.lang.app.db.SQLiteHelper.*;

public class DbWStorage extends DbBaseRStorage
{
	private SQLiteDatabase mDb;
	
	public DbWStorage (Context context)
	{
		SQLiteHelper helper = new SQLiteHelper(context);
		mDb = helper.getWritableDatabase();
		mDb.execSQL("PRAGMA foreign_keys=ON;");
		
		super.setDb(mDb);
	}
	
	public void recycle ()
	{
		mDb.close();
		mDb = null;
	}

	public void addWord (String nativ, String foreign)
	{
		mDb.delete(TNAME_WORDS, WordsCols.NATIV + " = ?", new String[]{nativ});
		
		ContentValues values = new ContentValues();
		values.put(WordsCols.NATIV, nativ);
		values.put(WordsCols.FOREIGN, foreign);
		values.put(WordsCols.RATING, 0);
		mDb.insert(TNAME_WORDS, null, values);
	}
}
