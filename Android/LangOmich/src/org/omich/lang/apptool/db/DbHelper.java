package org.omich.lang.apptool.db;

import org.omich.tool.events.Listeners.IListener;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbHelper
{
	abstract public static class CursorIterator implements IListener<Cursor>
	{
		private boolean mIsTerminated;
		
		protected void terminate () {mIsTerminated = true;}
		public boolean isTerminated () {return mIsTerminated;}
	}
	
	public static Cursor queryBySingleColumn (SQLiteDatabase db, String table,
			String [] columns, String searchColumn, String searchValue)
	{
		return db.query(table, columns, 
				searchColumn + "=\"" + searchValue + "\"", 
				null, null, null, null);
	}
	
	public static void iterateCursorAndClose (Cursor cursor, CursorIterator iterator)
	{
		cursor.moveToFirst();
		while(!cursor.isAfterLast() && !iterator.isTerminated())
		{
			iterator.handle(cursor);
			cursor.moveToNext();
		}
		cursor.close();
	}
}
