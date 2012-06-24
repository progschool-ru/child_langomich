package org.omich.lang.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static org.omich.lang.app.db.SQLiteHelper.*;

public class DbWStorage extends DbBaseRStorage implements IWStorage
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

	public long addWord (String nativ, String foreign)
	{
		String query = "SELECT " + DictsCols.ID + " FROM " + TNAME_DICTS 
				+ " LIMIT 0, 1";
		Cursor cursor = mDb.rawQuery(query, null);
		cursor.moveToFirst();
		long dictId = cursor.getLong(0);
		cursor.close();

		mDb.delete(TNAME_WORDS, WordsCols.DICT_ID + " = " + dictId + " AND " 
						+ WordsCols.NATIV + " = ? ", new String[]{nativ});
		
		ContentValues values = new ContentValues();
		values.put(WordsCols.NATIV, nativ);
		values.put(WordsCols.FOREIGN, foreign);
		values.put(WordsCols.RATING, 0);
		values.put(WordsCols.DICT_ID, dictId);
		return mDb.insert(TNAME_WORDS, null, values);
	}
}
