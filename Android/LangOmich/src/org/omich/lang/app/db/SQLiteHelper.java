package org.omich.lang.app.db;

import java.util.Date;

import org.omich.tool.log.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper
{
//	private static final String CREATE_INDEX_TEMPLATE = "create index %1$s_index on %2$s (%1$s)";

	private static final String DATABASE_NAME = "app.db";
	private static final int    DATABASE_VERSION = 1;

	public static final String TNAME_DICTS = "dicts";
	public static final String TNAME_WORDS = "words";
	
	private static final String CREATE_DICTS_QUERY = "create table "
			+ TNAME_DICTS + "("
				+ DictsCols.ID              + " integer not null primary key autoincrement, "
				+ DictsCols.TIME            + " integer not null, "
				+ DictsCols.SERVER_ID       + " text unique, "
				+ DictsCols.NAME            + " text unique)";

	private static final String CREATE_WORDS_QUERY = "create table "
			+ TNAME_WORDS + "("
				+ WordsCols.ID              + " integer not null primary key autoincrement, "
				+ WordsCols.DICT_ID         + " integer not null, "
				+ WordsCols.RATING          + " integer not null, "
				+ WordsCols.TIME            + " integer not null, "
				+ WordsCols.NATIV           + " text unique, "
				+ WordsCols.FOREIGN         + " text, "
				+ "foreign key (" + WordsCols.DICT_ID + ") references " 
						+ TNAME_DICTS + "(" + DictsCols.ID + ") on delete cascade)";

	public static final class DictsCols
	{
		public static final String ID          = "_id";
		public static final String NAME        = "name";
		public static final String SERVER_ID   = "serverId";
		public static final String TIME        = "time";
	}

	public static final class WordsCols
	{
		public static final String ID          = "_id";
		public static final String NATIV       = "nativ";
		public static final String FOREIGN     = "forein";
		public static final String RATING      = "rating";
		public static final String DICT_ID     = "dictId";
		public static final String TIME        = "time";
	}
	
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
		database.execSQL(CREATE_DICTS_QUERY);
		database.execSQL(CREATE_WORDS_QUERY);
//		database.execSQL(String.format(CREATE_INDEX_TEMPLATE, LgsCols.SERVER_ID, LGS_NAME));		
		createDefaultDict(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		Log.wtf(this.getClass(), "Should never try to upgrade in this version!"
				+ "oldV: " + oldVersion + ", newV: " + newVersion, null);
	}
	
	private void createDefaultDict (SQLiteDatabase db)
	{
		ContentValues cv = new ContentValues();
		cv.put(DictsCols.NAME, "default");
		Long time = new Date().getTime();
		cv.put(DictsCols.TIME, time);
		db.insert(TNAME_DICTS, null, cv);
	}
}
