package org.smdserver.words;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.smdserver.db.DbException;
import org.smdserver.db.IMultipleResultParser;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.ISmdStatement;
import org.smdserver.db.SmdStatement;

public class WordsDBStorage implements IWordsStorage
{
	private static final String LANGUAGES_TABLE = "languages";
	private static final String WORDS_TABLE = "words";

	private static final String ADD_LANGUAGE_QUERY = "INSERT INTO %1$s (language_id, name, user_id, modified, time_created, time_modified) VALUE (?, ?, ?, ?, NOW(), NOW());";
	private static final String ADD_WORD_QUERY     = "INSERT INTO %1$s (translation, rating, modified, language_id, original, time_created, time_modified) VALUE (?, ?, ?, ?, ?, NOW(), NOW());";
	private static final String UPDATE_WORD_QUERY = "UPDATE %1$s SET translation=?, rating=?, modified=?, time_modified=NOW() WHERE language_id=? AND original=?;";
	private static final String GET_ALL_WORDS_WITH_EMPTY_LANGUAGES = "SELECT * FROM %1$s as w RIGHT OUTER JOIN %2$s as l ON l.language_id = w.language_id WHERE l.user_id=?;";
	private static final String GET_LATEST_WORDS_WITH_EMPTY_LANGUAGES = "SELECT * FROM %1$s as w RIGHT OUTER JOIN %2$s as l ON l.language_id = w.language_id " +
																		" WHERE l.user_id=? AND " +
																		" (w.original IS NULL     AND l.modified > ?     OR" +
																		"  w.original IS NOT NULL AND w.modified > ?);";
	private static final String GET_WORDS_IN     = "SELECT original FROM %1$s WHERE language_id = ? AND original IN (%2$s);";
	private static final String CLEAR_LANGUAGES = "DELETE FROM %1$s WHERE user_id = ?;";
	private static final String GET_LANGUAGES_IN = "SELECT language_id FROM %1s WHERE language_id IN (%2$s);";

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
			Set<String> existedLanguages = getExistedObjects(
					languages, 
					GET_LANGUAGES_IN,
					new LanguageInVisitor(),
					null);

			ISmdStatement st = new SmdStatement();
			int addLanguageIndex = st.addQuery(String.format(ADD_LANGUAGE_QUERY, languagesTable));
			int updateWordIndex = st.addQuery(String.format(UPDATE_WORD_QUERY, wordsTable));
			int addWordIndex = st.addQuery(String.format(ADD_WORD_QUERY, wordsTable));

			for(Language language : languages)
			{
				Set<String> existedWords;
				String languageId = language.getId();

				if(languageId == null || !existedLanguages.contains(languageId))
				{
					if(languageId == null)
					{
						languageId = UUID.randomUUID().toString();//TODO: (3.low)[#26069] use common util for creation Ids.
					}

					st.startSet(addLanguageIndex);
					st.addString(languageId);
					st.addString(language.getName());
					st.addString(userId);
					st.addLong(currentTime + 1);// Единицу добавляем, чтобы извлечь его при следующем запросе.
					
					language.setId(languageId);
					existedWords = new HashSet<String>();
				}
				else
				{
					existedWords = getExistedObjects(language.getWords(),
							GET_WORDS_IN,
							new WordInVisitor(),
							languageId);
				}

				List<Word> words = language.getWords();
				for(Word word : words)
				{
					st.startSet(existedWords.contains(word.getOriginal()) 
								? updateWordIndex : addWordIndex);
					st.addString(word.getTranslation());
					st.addInteger(word.getRating());
					st.addLong(currentTime);
					st.addString(languageId);
					st.addString(word.getOriginal());
				}
			}

			int count = db.processSmdStatement(st);
			return count >= 0;
		}
		catch(DbException e)
		{
			return false;
		}
	}

	public List<Language> getLatestUserWords(String userId, long lastModified)
	{
		ISmdStatement st = createSmdStatement(GET_LATEST_WORDS_WITH_EMPTY_LANGUAGES);
		st.addString(userId);
		st.addLong(lastModified);
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
	
	private String getInTemplate(int size)
	{
		StringBuilder sb = new StringBuilder();
		boolean isNotAFirst = false;
		for(int i = 0; i < size; i++)
		{
			if(isNotAFirst)
			{
				sb.append(',');
			}
			else
			{
				isNotAFirst = true;
			}
			sb.append('?');
		}
		return sb.toString();
	}
	
	private Set<String> getExistedObjects(List list, String template, 
			                              IInVisitor vis, String additionalParam)
			                      throws DbException
	{
		if(list.isEmpty())
		{
			return new HashSet<String>();
		}
		
		String inList = getInTemplate(list.size());
		String query = String.format(template, wordsTable, inList);
		ISmdStatement st = new SmdStatement();
		st.addQuery(query);
		st.startSet(0);
		if(additionalParam != null)
		{
			st.addString(additionalParam);
		}
		for(Object object : list)
		{
			st.addString(vis.getInString(object));
		}
		SetParser parser = new SetParser();
		db.select(st, parser);
		return parser.set;	
	}

	private interface IInVisitor
	{
		String getInString(Object value);
	}

	private class WordInVisitor implements IInVisitor
	{
		public String getInString(Object word)
		{
			return ((Word)word).getOriginal();
		}
	}

	private class LanguageInVisitor implements IInVisitor
	{
		public String getInString(Object language)
		{
			return ((Language)language).getId();
		}
	}

	private class SetParser implements IMultipleResultParser
	{
		Set<String> set;

		public int parse(ResultSet result) throws SQLException
		{
			set = new HashSet<String>();
			while(result.next())
			{
				set.add(result.getString(1));
			}
			return set.size();
		}
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
