package org.smdserver.words;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smdserver.db.DbException;
import org.smdserver.db.IMultipleResultParser;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.ISmdStatement;
import org.smdserver.db.SmdStatement;
import org.smdserver.words.helpers.LanguageSaver;
import org.smdserver.words.helpers.LanguagesDistributor;

public class WordsDBStorage implements IWordsStorage
{
	private static final String LANGUAGES_TABLE = "languages";
	private static final String WORDS_TABLE = "words";

	private static final String ADD_LANGUAGE_QUERY = "INSERT INTO %1$s (language_id, name, user_id, modified, time_created, time_modified) VALUE (?, ?, ?, ?, NOW(), NOW());";
	private static final String ADD_WORD_QUERY     = "INSERT INTO %1$s (translation, rating, modified, language_id, original, time_created, time_modified) VALUE (?, ?, ?, ?, ?, NOW(), NOW());";
	private static final String GET_ALL_WORDS_WITH_EMPTY_LANGUAGES = "SELECT * FROM %1$s as w RIGHT OUTER JOIN %2$s as l ON l.language_id = w.language_id AND w.translation <> \"\" WHERE l.user_id=?;";
	private static final String GET_LATEST_WORDS_WITH_EMPTY_LANGUAGES = "SELECT * FROM %1$s as w RIGHT OUTER JOIN %2$s as l ON l.language_id = w.language_id AND w.modified > ?" +
																		" WHERE l.user_id=? AND " +
																		" (w.original IS NULL     AND l.modified > ?     OR" +
																		"  w.original IS NOT NULL );";
	private static final String CLEAR_LANGUAGES = "DELETE FROM %1$s WHERE user_id = ?;";

	private ISmdDB db;
	private String languagesTable;
	private String wordsTable;

	public WordsDBStorage(ISmdDB db, String prefix)
	{
		this.db = db;
		languagesTable = prefix + LANGUAGES_TABLE;
		wordsTable = prefix + WORDS_TABLE;
	}
	
	public boolean addUserWords(String userId, List<Language> languages, long currentTime)
	{
		try
		{
			LanguagesDistributor distributor = new LanguagesDistributor(languagesTable, userId);
			distributor.handleList(languages, db);
			LanguageSaver saver = new LanguageSaver(distributor, languagesTable, wordsTable, currentTime, userId, db);
			return saver.save();
		}
		catch(DbException e)
		{
			return false;
		}
	}

	public List<Language> getLatestUserWords(String userId, long lastModified)
	{
		if(lastModified <= 0)
		{
			return getUserWords(userId);
		}
		
		ISmdStatement st = createSmdStatement(GET_LATEST_WORDS_WITH_EMPTY_LANGUAGES);
		st.addLong(lastModified);
		st.addString(userId);
		st.addLong(lastModified);
		return getWords(st);
	}

	public List<Language> getUserWords(String userId)
	{
		ISmdStatement st = createSmdStatement(GET_ALL_WORDS_WITH_EMPTY_LANGUAGES);
		st.addString(userId);
		return getWords(st);
	}

	public boolean setUserWords(String userId, List<Language> languages, long currentTime)
	{
		ISmdStatement st = new SmdStatement();

		st.addQuery(String.format(CLEAR_LANGUAGES, languagesTable));
		st.startSet(0);
		st.addString(userId);

		int addLangIndex = st.addQuery(String.format(ADD_LANGUAGE_QUERY, languagesTable));
		int addWordIndex = st.addQuery(String.format(ADD_WORD_QUERY, wordsTable));

		for(Language language : languages)
		{
			st.startSet(addLangIndex);
			st.addString(language.getId());
			st.addString(language.getName());
			st.addString(userId);
			st.addLong(currentTime);

			List<Word> words = language.getWords();
			for(Word word : words)
			{
				st.startSet(addWordIndex);
				st.addString(word.getTranslation());
				st.addInteger(word.getRating());
				st.addLong(currentTime);
				st.addString(language.getId());
				st.addString(word.getOriginal());
			}
		}

		try
		{
			int count = db.processSmdStatement(st);
			return count >= 0;
		}
		catch(DbException e)
		{
			return false;
		}
	}

	public boolean setUserWords(String userId, List<Language> languages)
	{
		return setUserWords(userId, languages, new Date().getTime());
	}

	private List<Language> getWords(ISmdStatement st)
	{
		LanguagesCreatorParser parser = new LanguagesCreatorParser();
		try
		{
			db.select(st, parser);
		}
		catch(DbException e)
		{
			return null;
		}
		return parser.languages;		
	}
		
	private ISmdStatement createSmdStatement(String query)
	{
		ISmdStatement st = new SmdStatement();
		st.addQuery(String.format(query, wordsTable, languagesTable));
		st.startSet(0);
		return st;
	}

	private class LanguagesCreatorParser implements IMultipleResultParser
	{
		List<Language> languages;

		public int parse(ResultSet set) throws SQLException
		{
			Map<String, Language> langs = new HashMap<String, Language>();
			languages = new ArrayList<Language>();
			
			int count = 0;
			while(set.next())
			{
				Language language = getOrCreateLanguage(langs, set);
				Word word = createWord(set);
				if(word != null)
				{
					language.getWords().add(word);
				}
				count ++;
			}
			return count;
		}

		private Language getOrCreateLanguage(Map<String, Language> langs, ResultSet set)
				throws SQLException
		{
			String languageId = set.getString("l.language_id");
			if(langs.containsKey(languageId))
			{
				return langs.get(languageId);
			}
			else
			{
				Language language = new Language(languageId, set.getString("l.name"));
				langs.put(languageId, language);
				languages.add(language);
				return language;
			}
		}

		private Word createWord(ResultSet set) throws SQLException
		{
			String original = set.getString("w.original");

			if(original == null)
				return null;

			String translation = set.getString("w.translation");
			int rating = set.getInt("w.rating");

			Word word = new Word(original, translation, rating);
			return word;
		}
	}
}
