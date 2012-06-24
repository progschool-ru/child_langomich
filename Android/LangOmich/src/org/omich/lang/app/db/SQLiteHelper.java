package org.omich.lang.app.db;

import org.omich.tool.log.Log;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "app.db";
	private static final int    DATABASE_VERSION = 1;

	public static final String TNAME_WORDS = "words";
	
	public static final class WordsCols
	{
		public static final String ID              = "_id";
		public static final String NATIV           = "nativ";
		public static final String FOREIGN         = "forein";
		public static final String RATING          = "rating";
	}
	
	private static final String CREATE_WORDS_QUERY = "create table "
			+ TNAME_WORDS + "("
				+ WordsCols.ID              + " integer not null primary key autoincrement, "
				+ WordsCols.RATING          + " integer not null, "
				+ WordsCols.NATIV           + " text unique, "
				+ WordsCols.FOREIGN         + " text)";
	
	//=========================================================================
	public SQLiteHelper(Context context, String dbName, int version) 
	{
		super(context, dbName, null, version);
	}

	public SQLiteHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//==== SQLiteOpenHelper ===================================================
	@Override
	public void onCreate(SQLiteDatabase database) 
	{
		database.execSQL(CREATE_WORDS_QUERY);
//		database.execSQL(String.format(CREATE_INDEX_TEMPLATE, LgsCols.SERVER_ID, LGS_NAME));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		Log.wtf(this.getClass(), "Should never try to upgrade in this version!"
				+ "oldV: " + oldVersion + ", newV: " + newVersion, null);
	}
}
