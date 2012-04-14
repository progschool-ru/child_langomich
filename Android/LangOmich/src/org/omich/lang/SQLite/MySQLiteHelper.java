package org.omich.lang.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySQLiteHelper extends SQLiteOpenHelper  {
	
	public static final String WORDS_TABLE = "words";
	
	public static final String WORD_ID = "id";
	public static final String ORIGINAL = "original";
	public static final String TRANSLATION = "translation";
	public static final String RATING = "rating";
	public static final String MODIFIED = "modified";
	public static final String WORDS_LANGUAGE = "languages_id";
	public static final String WORD_IN_SERVER = "words_in_server";
	
	
	
	public static final String LANGUAGES_TABLE = "languages";
	
	public static final String LANGUAGE_ID = "id";
	public static final String NAME = "name";
	public static final String LANGUAGE_SERVER_ID = "language_id";
	
	private static final String DATABASE_NAME = "langOmich.db";
	private static final int DATABASE_VERSION = 1;
	
	
	private static final String CREATE_LANGUAGES_TABLE = "CREATE TABLE "+ LANGUAGES_TABLE
			+" ( "
				+ LANGUAGE_ID + "  integer primary key autoincrement, "
				+ LANGUAGE_SERVER_ID +" text unique, "
				+ NAME  +" text not null " 
			+" )";
	
	private static final String CREATE_WORDS_TABLE = "CREATE TABLE " + WORDS_TABLE
			+ "( "
				+ WORD_ID + " integer primary key autoincrement, "
				+ ORIGINAL + " text not null, "
				+ TRANSLATION + " text, "
				+ WORDS_LANGUAGE + " integer references "+LANGUAGES_TABLE+"("+LANGUAGE_ID+") on delete cascade, "
				+ RATING +" integer, "
				+ MODIFIED + " integer,"
				+ WORD_IN_SERVER +"integer"
			+")";
	
	public MySQLiteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase database){
		database.execSQL(CREATE_LANGUAGES_TABLE);
        database.execSQL(CREATE_WORDS_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS" + WORDS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS" + LANGUAGES_TABLE);
		onCreate(db);
	}
}
