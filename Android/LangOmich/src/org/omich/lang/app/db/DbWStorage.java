package org.omich.lang.app.db;

import java.util.Date;
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
	
	@Override
	public void destroy ()
	{
		mDb.close();
		mDb = null;
	}
	
	@Override
	public long addWord (String nativ, String foreign, long dictId)
	{
		nativ = nativ.trim();
		foreign = foreign.trim();
		if(dictId == -1)
		{
			String query = "SELECT " + DictsCols.ID + " FROM " + TNAME_DICTS 
					+ " LIMIT 0, 1";
			Cursor cursor = mDb.rawQuery(query, null);
			cursor.moveToFirst();
			dictId = cursor.getLong(0);
			cursor.close();
		}
		return addWord(nativ, foreign, 0, dictId);
	}
	@Override
	public long addWord (String nativ, String foreign, int rating, long dictId)
	{	
		nativ = nativ.trim();
		foreign = foreign.trim();
		mDb.delete(TNAME_WORDS, WordsCols.DICT_ID + " = " + dictId + " AND " 
				+ WordsCols.FOREIGN + " = ? ", new String[]{foreign});
			
		Long time = new Date().getTime();
		long id = 0;
		if(nativ != null && !nativ.equals("") && foreign != null && !foreign.equals(""))
		{
			ContentValues values = new ContentValues();		
			values.put(WordsCols.NATIV, nativ);
			values.put(WordsCols.FOREIGN, foreign);
			values.put(WordsCols.RATING, rating);
			values.put(WordsCols.DICT_ID, dictId);
			values.put(WordsCols.TIME, time);
			id = mDb.insert(TNAME_WORDS, null, values);
		}
		dictTimeUpdate(dictId, time);
			
		return id;
	}	
	@Override
	public boolean deleteWord(long id)
	{
		try
		{
			Long time = new Date().getTime();
			dictTimeUpdate(getDictIdByWordId(id), time);
			
			ContentValues values = new ContentValues();
			values.put(WordsCols.NATIV, "");		
			values.put(WordsCols.FOREIGN, "");
			values.put(WordsCols.TIME, time);
			String where = WordsCols.ID + "=" + id;
			mDb.update(TNAME_WORDS, values, where, null);	
			return true;
		}
		catch(Exception e) {return false;}
	}
	@Override
	public boolean changeWord(long id, String nativ, String foreign, long dictId)
	{
		try
		{
			Long time = new Date().getTime();
			dictTimeUpdate(getDictIdByWordId(id), time);
			
			ContentValues values = new ContentValues();
			values.put(WordsCols.NATIV, nativ);	
			values.put(WordsCols.FOREIGN, foreign);	
			values.put(WordsCols.DICT_ID, dictId);	
			values.put(WordsCols.TIME, time);
			String where = WordsCols.ID + " = " + id;
			mDb.update(TNAME_WORDS, values, where, null);	
			return true;
		}
		catch(Exception e) {return false;}
	}	
	public boolean moveWord(long id, long dictId)
	{
		try
		{
			Long time = new Date().getTime();
			dictTimeUpdate(getDictIdByWordId(id), time);
			dictTimeUpdate(dictId, time);
			
			ContentValues values = new ContentValues();
			values.put(WordsCols.DICT_ID, dictId);	
			values.put(WordsCols.TIME, time);
			String where = WordsCols.ID + " = " + id;
			mDb.update(TNAME_WORDS, values, where, null);	
			return true;
		}
		catch(Exception e) {return false;}
	}		
	public boolean copyWord(String nativ, String foreign,int rating, long dictId)
	{
		try
		{
			addWord(nativ, foreign, rating, dictId);
			return true;
		}
		catch(Exception e) {return false;}
	}	
	@Override
	public long addDict (String name)
	{		
		name = name.trim();
		String where = DictsCols.NAME + "= ?";
		Cursor cursor = mDb.query(TNAME_DICTS, new String[]{DictsCols.ID}, 
				where, new String[]{name}, null, null, null);	
		if(cursor.moveToFirst())
			return -1;
		ContentValues values = new ContentValues();
		values.put(DictsCols.NAME, name);
		Long time = new Date().getTime();
		values.put(DictsCols.TIME, time);
		return mDb.insert(TNAME_DICTS, null, values);
	}	
	@Override
	public long addDict (String serverId, String name)
	{
		name = name.trim();
		String where = DictsCols.SERVER_ID + "= ?";
		Cursor cursor = mDb.query(TNAME_DICTS, new String[]{DictsCols.ID}, 
				where, new String[]{serverId}, null, null, null);	
		long dictId = 0;
		if(cursor.moveToFirst()) // изменение имени словаря (при изменении имени на сервере)
		{
			ContentValues values = new ContentValues();		
			values.put(DictsCols.NAME, name);
			Long time = new Date().getTime();
			values.put(DictsCols.TIME, time);
			mDb.update(TNAME_DICTS, values, where, null);
			dictId = cursor.getLong(0);
		}
		else
		{
			where = DictsCols.NAME + "= ?";
			cursor = mDb.query(TNAME_DICTS, new String[]{DictsCols.ID}, 
					where, new String[]{name}, null, null, null);	
			if(cursor.moveToFirst()) // присваивание serverId новому словарю созданому на мобильном (или повоторное присваивание)
			{
				ContentValues values = new ContentValues();		
				values.put(DictsCols.SERVER_ID, serverId);
				Long time = new Date().getTime();
				values.put(DictsCols.TIME, time);
				mDb.update(TNAME_DICTS, values, where, null);
				dictId = cursor.getLong(0);
			}
			else // создание нового словаря на мобильном по имени и id словаря созданого на сервере
			{
				ContentValues values = new ContentValues();
				values.put(DictsCols.NAME, name);
				values.put(DictsCols.SERVER_ID, serverId);
				Long time = new Date().getTime();
				values.put(DictsCols.TIME, time);
		
				dictId =  mDb.insert(TNAME_DICTS, null, values);					
			}
		}	
		cursor.close();
		return dictId;
	}	
	@Override
	public boolean setRating (long id, int rating)
	{
		try
		{		
			Long time = new Date().getTime();
			dictTimeUpdate(getDictIdByWordId(id), time);
			
			ContentValues values = new ContentValues();
			values.put(WordsCols.RATING, rating);		
			values.put(WordsCols.TIME, time);
			String where = WordsCols.ID + " = " + id;
			mDb.update(TNAME_WORDS, values, where, null);
			return true;
		}
		catch(Exception e) {return false;}		
	}
	private void dictTimeUpdate(long dictId, long time)
	{
		if(dictId != -1)
		{
			ContentValues valuesForDict = new ContentValues();
			valuesForDict.put(DictsCols.TIME, time);
			String where = DictsCols.ID + " = " + dictId;
			mDb.update(TNAME_DICTS, valuesForDict, where, null);
		}			
	}
	private long getDictIdByWordId(long wordId)
	{
		long dictId = -1;
		String where = WordsCols.ID + " = " + wordId;		
		Cursor cursor = mDb.query(TNAME_WORDS, new String[]{WordsCols.DICT_ID}, 
				where, null, null, null, null);	
		if(cursor.moveToFirst())
			dictId = cursor.getLong(0);
		cursor.close();
		return dictId;
	}
}