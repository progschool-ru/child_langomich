package org.smdserver.words;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smdserver.db.DbException;
import org.smdserver.db.IMultipleResultParser;
import org.smdserver.db.ISmdDB;

public class WordsDBStorage implements IWordsStorage
{
	private static final String LANGUAGES_TABLE = "languages";
	private static final String WORDS_TABLE = "words";

	private static String ADD_LANGUAGE_QUERY = "INSERT INTO %1$s (language_id, name, user_id, time_created, time_modified) VALUE (\"%2$s\", \"%3$s\", \"%4$s\", NOW(), NOW());";
	private static String ADD_WORD_QUERY = "INSERT INTO %1$s (word_id, language_id, original, translation, rating, modified, time_created, time_modified) VALUE (\"%2$s\", \"%3$s\", \"%4$s\", \"%5$s\", %6$d, %7$d, NOW(), NOW());";
	private static String UPDATE_WORD_QUERY = "UPDATE %1$s SET original=\"%3$s\", translation=\"%4$s\", rating=%5$d, modified=%5$d, time_modified=NOW() WHERE word_id=\"%2$s\";";
	private static String GET_ALL_WORDS = "SELECT * FROM %1$s as l, %2$s as w, WHERE l.language_id = w.language_id AND l.user_id=\"%3$s\";";
	private static String GET_LATEST_WORDS = "SELECT * FROM %1$s as l, %2$s as w, WHERE l.language_id = w.language_id AND l.user_id=\"%3$s\" AND w.modified > %4$d;";

	private ISmdDB db;
	private String languagesTable;
	private String wordsTable;

	public WordsDBStorage(ISmdDB db, String prefix)
	{
		this.db = db;
		languagesTable = prefix + LANGUAGES_TABLE;
		wordsTable = prefix + WORDS_TABLE;
	}

	public boolean addUserWords(String userId, List<Language> languages)
	{
		return setWords(userId, languages, ADD_WORD_QUERY, ADD_LANGUAGE_QUERY);
	}

	public List<Language> getCopyUserWords(String userId)
	{
		return getUserWords(userId);
	}

	public List<Language> getCopyUserWords(String userId, long lastModified)
	{
		String query = String.format(GET_LATEST_WORDS, wordsTable, languagesTable, userId, lastModified);
		return getWords(query);
	}

	public List<Language> getUserWords(String userId)
	{
		String query = String.format(GET_ALL_WORDS, wordsTable, languagesTable, userId);
		return getWords(query);
	}

	public void setUserWords(String userId, List<Language> languages)
	{
		setWords(userId, languages, UPDATE_WORD_QUERY, null);
	}

	private List<Language> getWords(String query)
	{
		LanguagesCreatorParser parser = new LanguagesCreatorParser();
		try
		{
			db.select(query, parser);
		}
		catch(DbException e)
		{
			return null;
		}
		return parser.languages;
	}

	public boolean setWords(String userId, List<Language> languages,
							String wQuery, String lQuery)
	{
		List<String> queries = new ArrayList<String>();
		
		for(Language language : languages)
		{
			if(lQuery != null)
			{
				String languageQuery = String.format(lQuery, languagesTable,
											language.getId(), language.getName(),
											userId);
				queries.add(languageQuery);
			}

			if(wQuery != null)
			{
				List<Word> words = language.getWords();
				for(Word word : words)
				{
					String wordQuery = String.format(wQuery, wordsTable,
												word.getId(), word.getOriginal(),
												word.getTranslation(),
												word.getRating());
					queries.add(wordQuery);
				}
			}
		}

		try
		{
			int count = db.updateGroup(queries);
			return count >= 0;
		}
		catch(DbException e)
		{
			return false;
		}
	}

	private class LanguagesCreatorParser implements IMultipleResultParser
	{
		List<Language> languages;

		public int parse(ResultSet set) throws SQLException
		{
			Map<String, Language> langs = new HashMap<String, Language>();
			int count = 0;
			while(set.next())
			{
				Language language = getOrCreateLanguage(langs, set);
				Word word = createWord(set);
				language.getWords().add(word);
				count ++;
			}
			return count;
		}

		private Language getOrCreateLanguage(Map<String, Language> langs, ResultSet set)
				throws SQLException
		{
			String languageId = set.getString("w.language_id");
			if(langs.containsKey(languageId))
			{
				return langs.get(languageId);
			}
			else
			{
				Language language = new Language(languageId, set.getString("l.name"));
				langs.put(languageId, language);
				return language;
			}
		}

		private Word createWord(ResultSet set) throws SQLException
		{
			String id = set.getString("w.word_id");
			String original = set.getString("w.original");
			String translation = set.getString("w.translation");
			int rating = set.getInt("w.rating");
			long modified = set.getLong("w.modified");

			Word word = new Word(id, original, translation, rating, modified);
			return word;
		}
	}
}
