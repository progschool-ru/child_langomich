package org.omich.lang.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySQLiteHelper extends SQLiteOpenHelper  {
	
	public static final String TABLE_WORDS = "words";
	public static final String WORD_ID = "_id";
	public static final String ORIGINAL = "original";
	public static final String TRANSLATION = "translation";
	public static final String LANGUAGE = "language";
	
	private static final String DATABASE_NAME = "langOmich.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "create table "+ TABLE_WORDS + "( "
			+ WORD_ID + " integer primary key autoincrement, " + ORIGINAL + " text not null, " + 
			TRANSLATION +" text not null, "+ LANGUAGE + " text not null);";
	
	public MySQLiteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database){
		database.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_WORDS);
		onCreate(db);
	}
}
