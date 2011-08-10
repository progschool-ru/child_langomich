package org.smdserver.words;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

	private static final String ADD_LANGUAGE_QUERY = "INSERT INTO %1$s (language_id, name, user_id, time_created, time_modified) VALUE (?, ?, ?, NOW(), NOW());";
	private static final String ADD_WORD_QUERY     = "INSERT INTO %1$s (translation, rating, modified, language_id, original, time_created, time_modified) VALUE (?, ?, ?, ?, ?, NOW(), NOW());";
	private static final String UPDATE_WORD_QUERY = "UPDATE %1$s SET translation=?, rating=?, modified=?, time_modified=NOW() WHERE language_id=? AND original=?;";
	private static final String GET_ALL_WORDS    = "SELECT * FROM %1$s as w, %2$s as l WHERE l.language_id = w.language_id AND l.user_id=?;";
	private static final String GET_LATEST_WORDS = "SELECT * FROM %1$s as w, %2$s as l WHERE l.language_id = w.language_id AND l.user_id=? AND w.modified > ?;";
	private static final String GET_WORDS_IN     = "SELECT original FROM %1$s WHERE language_id = \"%2$s\" AND original IN (%3$s);";
	private static final String CLEAR_LANGUAGES = "DELETE FROM %1$s WHERE user_id = ?;";
	private static final String GET_LANGUAGES_IN = "SELECT language_id FROM %1s WHERE language_id in (%2$s);";

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
		try
		{
			String inLangList = getInString(languages, new LanguageInVisitor());
			String getLangsQuery = String.format(GET_LANGUAGES_IN, wordsTable, inLangList);
			SetParser langParser = new SetParser();
			db.select(getLangsQuery, langParser);
			Set<String> existedLanguagess = langParser.set;

			ISmdStatement st = new SmdStatement();
			int addLanguageIndex = st.addQuery(String.format(ADD_LANGUAGE_QUERY, languagesTable));
			int updateWordIndex = st.addQuery(String.format(UPDATE_WORD_QUERY, wordsTable));
			int addWordIndex = st.addQuery(String.format(ADD_WORD_QUERY, wordsTable));

			for(Language language : languages)
			{
				Set<String> existedWords;
				String languageId = language.getId();

				if(languageId == null || !existedLanguagess.contains(languageId))
				{
					if(languageId == null)
					{
						languageId = UUID.randomUUID().toString();//TODO: (3.low)[#26069] use common util for creation Ids.
					}

					st.startSet(addLanguageIndex);
					st.addString(languageId);
					st.addString(language.getName());
					st.addString(userId);
					
					language.setId(languageId);
					existedWords = new HashSet<String>();
				}
				else
				{
					IInVisitor v = new WordInVisitor();
					String inList = getInString(language.getWords(), v);
					String getWordsQuery = String.format(GET_WORDS_IN, wordsTable,
												languageId, inList);
					SetParser parser = new SetParser();
					db.select(getWordsQuery, parser);
					existedWords = parser.set;
				}

				List<Word> words = language.getWords();
				for(Word word : words)
				{
					st.startSet(existedWords.contains(word.getOriginal()) 
								? updateWordIndex : addWordIndex);
					st.addString(word.getTranslation());
					st.addInteger(word.getRating());
					st.addLong(word.getModified());
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

	public List<Language> getCopyUserWords(String userId)
	{
		return getUserWords(userId);
	}

	public List<Language> getCopyUserWords(String userId, long lastModified)
	{
		ISmdStatement st = createSmdStatement(GET_LATEST_WORDS);
		st.addString(userId);
		st.addLong(lastModified);
		return getWords(st);
	}

	public List<Language> getUserWords(String userId)
	{
		ISmdStatement st = createSmdStatement(GET_ALL_WORDS);
		st.addString(userId);
		return getWords(st);
	}

	public boolean setUserWords(String userId, List<Language> languages)
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

			List<Word> words = language.getWords();
			for(Word word : words)
			{
				st.startSet(addWordIndex);
				st.addString(word.getTranslation());
				st.addInteger(word.getRating());
				st.addLong(word.getModified());				
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
	
	private String getInString(List list, IInVisitor visitor)
	{
		StringBuilder sb = new StringBuilder();
		boolean isNotAFirst = false;
		for(Object value : list)
		{
			if(isNotAFirst)
			{
				sb.append(',');
			}
			{
				isNotAFirst = true;
			}
			sb.append('\"');
			sb.append(visitor.getInString(value));
			sb.append('\"');
		}
		return sb.toString();
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
				languages.add(language);
				return language;
			}
		}

		private Word createWord(ResultSet set) throws SQLException
		{
			String original = set.getString("w.original");
			String translation = set.getString("w.translation");
			int rating = set.getInt("w.rating");
			long modified = set.getLong("w.modified");

			Word word = new Word(original, translation, rating, modified);
			return word;
		}
	}
}
