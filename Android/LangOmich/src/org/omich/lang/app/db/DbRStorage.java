package org.omich.lang.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbRStorage extends DbBaseRStorage
{
	private SQLiteDatabase mDb;
	
	public DbRStorage (Context context)
	{
		SQLiteHelper helper = new SQLiteHelper(context);
		mDb = helper.getReadableDatabase();
		mDb.execSQL("PRAGMA foreign_keys=ON;");
		
		super.setDb(mDb);
	}
	
	public void destroy ()
	{
		mDb.close();
		mDb = null;
	}
}
