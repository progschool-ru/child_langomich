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

public class WordsDBStorage implements IWordsStorage
{
	private static final String LANGUAGES_TABLE = "languages";
	private static final String WORDS_TABLE = "words";

	private static final String ADD_LANGUAGE_QUERY = "INSERT INTO %1$s (language_id, name, user_id, time_created, time_modified) VALUE (\"%2$s\", \"%3$s\", \"%4$s\", NOW(), NOW());";
	private static final String ADD_WORD_QUERY     = "INSERT INTO %1$s (language_id, original, translation, rating, modified, time_created, time_modified) VALUE (\"%2$s\", \"%3$s\", \"%4$s\", %5$d, %6$d, NOW(), NOW());";
	private static final String UPDATE_WORD_QUERY = "UPDATE %1$s SET translation=\"%4$s\", rating=%5$d, modified=%6$d, time_modified=NOW() WHERE language_id=\"%2$s\" AND original=\"%3$s\";";
	private static final String GET_ALL_WORDS    = "SELECT * FROM %1$s as w, %2$s as l WHERE l.language_id = w.language_id AND l.user_id=\"%3$s\";";
	private static final String GET_LATEST_WORDS = "SELECT * FROM %1$s as w, %2$s as l WHERE l.language_id = w.language_id AND l.user_id=\"%3$s\" AND w.modified > %4$d;";
	private static final String GET_WORDS_IN     = "SELECT original FROM %1$s WHERE language_id = \"%2$s\" AND original IN (%3$s);";
	private static final String CLEAR_LANGUAGES = "DELETE FROM %1$s WHERE user_id = \"%2$s\";";
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

			List<String> queries = new ArrayList<String>();

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
					String languageQuery = String.format(ADD_LANGUAGE_QUERY, languagesTable,
												languageId, language.getName(),
												userId);
					queries.add(languageQuery);
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
					String queryTemplate = existedWords.contains(word.getOriginal())
											? UPDATE_WORD_QUERY : ADD_WORD_QUERY;
					String wordQuery = 	String.format(queryTemplate, wordsTable,
												languageId,
												word.getOriginal(),
												word.getTranslation(),
												word.getRating(),
												word.getModified());
					queries.add(wordQuery);
				}
			}

			int count = db.updateGroup(queries);
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
		String query = String.format(GET_LATEST_WORDS, wordsTable, languagesTable, userId, lastModified);
		return getWords(query);
	}

	public List<Language> getUserWords(String userId)
	{
		String query = String.format(GET_ALL_WORDS, wordsTable, languagesTable, userId);
		return getWords(query);
	}

	public boolean setUserWords(String userId, List<Language> languages)
	{
		List<String> queries = new ArrayList<String>();

		String clearLanguage = String.format(CLEAR_LANGUAGES, languagesTable,
											userId);
		queries.add(clearLanguage);

		for(Language language : languages)
		{
			
			String languageQuery = String.format(ADD_LANGUAGE_QUERY, languagesTable,
											language.getId(), language.getName(),
											userId);
			queries.add(languageQuery);

			List<Word> words = language.getWords();
			for(Word word : words)
			{
				String wordQuery = String.format(ADD_WORD_QUERY, wordsTable,
											language.getId(),
											word.getOriginal(),
											word.getTranslation(),
											word.getRating(),
											word.getModified());
				queries.add(wordQuery);
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