package org.omich.lang.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySQLiteHelper extends SQLiteOpenHelper  {
	
	public static final String TABLE_WORDS = "words";
	

	public static final String ORIGINAL = "original";
	public static final String TRANSLATION = "translation";
	public static final String RATING = "rating";
	public static final String MODIFICATION = "modification";
	
	public static final String TABLE_LANGUAGES = "languages";
	
	public static final String NAME = "name";
	public static final String ID = "ID";
	
	private static final String DATABASE_NAME = "langOmich.db";
	private static final int DATABASE_VERSION = 1;
	
	
	public static final String TABLE_LANGUAGES_CREATE = " create table "+TABLE_LANGUAGES + "( "
			+ ID + " text not null primary key "
			+ NAME + " text not null)";
	
	private static final String TABLE_WORDS_CREATE = "create table "+ TABLE_WORDS + "( "
			+ ORIGINAL + " text not null, " + 
			TRANSLATION +" text not null, "+ 
			ID + " text not null,"+
			RATING + "integer,"+
			MODIFICATION + "integer);";
	
	public MySQLiteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database){
		//database.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_WORDS);
		onCreate(db);
	}
}
