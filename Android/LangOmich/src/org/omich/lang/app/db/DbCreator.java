package org.omich.lang.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteException;

public class DbCreator
{
	public static IRStorage createReadable (Context context)
	{
		try
		{
			return new DbRStorage(context);
		}
		catch (SQLiteException er)
		{
			return new DbWStorage(context);
		}
	}
	
	public static IWStorage createWritable (Context context)
	{
		return new DbWStorage(context);
	}
}
