package org.omich.lang.app.db;

import java.util.Date;
import java.util.List;

import org.omich.lang.app.db.SQLiteHelper.DictsCols;

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
	
	public void destroy ()
	{
		mDb.close();
		mDb = null;
	}

	public long addWord (String nativ, String foreign, long dictId)
	{
		if(dictId == -1)
		{
			String query = "SELECT " + DictsCols.ID + " FROM " + TNAME_DICTS 
					+ " LIMIT 0, 1";
			Cursor cursor = mDb.rawQuery(query, null);
			cursor.moveToFirst();
			dictId = cursor.getLong(0);
			cursor.close();
		}
		mDb.delete(TNAME_WORDS, WordsCols.DICT_ID + " = " + dictId + " AND " 
						+ WordsCols.NATIV + " = ? ", new String[]{nativ});
		
		ContentValues values = new ContentValues();
		values.put(WordsCols.NATIV, nativ);
		values.put(WordsCols.FOREIGN, foreign);
		values.put(WordsCols.RATING, 0);
		values.put(WordsCols.DICT_ID, dictId);
		Long time = new Date().getTime();
		values.put(WordsCols.TIME, time);
		return mDb.insert(TNAME_WORDS, null, values);
	}
	public void addWords (List<Word> words)
	{	
		for(int i = 0; i < words.size(); i++)
		{
			String where = WordsCols.NATIV + "=" + words.get(i).nativ;
			
			Cursor cursor = mDb.query(TNAME_WORDS, new String[]{WordsCols.ID}, 
					where, null, null, null, null);				
			if(cursor.moveToFirst())
			{
				ContentValues values = new ContentValues();		
				values.put(WordsCols.FOREIGN, words.get(i).foreign);
				values.put(WordsCols.RATING, 0);
				Long time = new Date().getTime();
				values.put(WordsCols.TIME, time);
				mDb.update(TNAME_WORDS, values, where, null);				
			}
			else
				addWord(words.get(i).nativ, words.get(i).foreign, words.get(i).id);
		}
	}		
	public long addDict (String name)
	{		
		ContentValues values = new ContentValues();
		values.put(DictsCols.NAME, name);
		values.put(DictsCols.SERVER_ID, -1);
		Long time = new Date().getTime();
		values.put(DictsCols.TIME, time);
		return mDb.insert(TNAME_DICTS, null, values);
	}	
	public void addDicts (List<Dict> dicts)
	{	
		for(int i = 0; i < dicts.size(); i++)
		{
			String where = DictsCols.SERVER_ID + "=" + dicts.get(i).serverId;
			
			Cursor cursor = mDb.query(TNAME_DICTS, new String[]{WordsCols.ID}, 
					where, null, null, null, null);	
			if(cursor.moveToFirst()) // изменение имени словаря (при изменении имени на сервере)
			{
				ContentValues values = new ContentValues();		
				values.put(DictsCols.NAME, dicts.get(i).name);
				Long time = new Date().getTime();
				values.put(DictsCols.TIME, time);
				mDb.update(TNAME_DICTS, values, where, null);
			}
			else
			{
				where = DictsCols.NAME + "=" + dicts.get(i).name;
				cursor = mDb.query(TNAME_DICTS, new String[]{WordsCols.ID}, 
						where, null, null, null, null);	
				if(cursor.moveToFirst()) // присваивание serverId новому словарю созданому на мобильном
				{
					ContentValues values = new ContentValues();		
					values.put(DictsCols.SERVER_ID, dicts.get(i).serverId);
					Long time = new Date().getTime();
					values.put(DictsCols.TIME, time);
					mDb.update(TNAME_DICTS, values, where, null);
				}
				else // создание нового словаря на мобильном по имени и id словаря созданого на сервере
				{
					ContentValues values = new ContentValues();
					values.put(DictsCols.NAME, dicts.get(i).name);
					values.put(DictsCols.SERVER_ID, dicts.get(i).serverId);
					Long time = new Date().getTime();
					values.put(DictsCols.TIME, time);
					mDb.insert(TNAME_DICTS, null, values);					
				}
			}
		}
	}	
	public void setRating (long id, int rating)
	{		
		ContentValues values = new ContentValues();		
		values.put(WordsCols.RATING, rating);
		Long time = new Date().getTime();
		values.put(WordsCols.TIME, time);
		String where = "_id=" + id;
		mDb.update(TNAME_WORDS, values, where, null);
	}
}
