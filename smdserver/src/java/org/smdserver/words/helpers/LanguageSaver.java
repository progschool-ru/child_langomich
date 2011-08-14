package org.smdserver.words.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.smdserver.db.DbException;
import org.smdserver.db.IMultipleResultParser;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.ISmdStatement;
import org.smdserver.db.SmdStatement;
import org.smdserver.words.Language;
import org.smdserver.words.Word;
import org.smdserver.words.helpers.LanguagesDistributor.SimpleLanguage;

public class LanguageSaver
{
	private static final String ADD_LANGUAGE_QUERY = "INSERT INTO %1$s (language_id, name, user_id, modified, time_created, time_modified) VALUE (?, ?, ?, ?, NOW(), NOW());";
	private static final String ADD_WORD_QUERY     = "INSERT INTO %1$s (translation, rating, modified, language_id, original, time_created, time_modified) VALUE (?, ?, ?, ?, ?, NOW(), NOW());";
	private static final String GET_WORDS_IN     = "SELECT original FROM %1$s WHERE language_id = ? AND original IN (%2$s);";
	private static final String UPDATE_WORD_QUERY = "UPDATE %1$s SET translation=?, rating=?, modified=?, time_modified=NOW() WHERE language_id=? AND original=?;";
	private static final String UPDATE_LANGUAGE_QUERY = "UPDATE %1$s SET name=?, modified=?, time_modified=NOW() WHERE language_id = ?";

	private ISmdStatement st;
	private int addLanguageIndex;
	private int updateLanguageIndex;
	private int addWordIndex;
	private int updateWordIndex;

	private LanguagesDistributor ld;
	private String languagesTable;
	private String wordsTable;
	private long currentTime;
	private String userId;
	private ISmdDB db;

	public LanguageSaver(LanguagesDistributor ld, String languagesTable, 
			             String wordsTable, long currentTime,
						 String userId, ISmdDB db)
	{
		this.ld = ld;
		this.languagesTable = languagesTable;
		this.wordsTable = wordsTable;
		this.currentTime = currentTime;
		this.userId = userId;
		this.db = db;
	}

	public boolean save() throws DbException
	{
		st = new SmdStatement();
		addLanguageIndex = st.addQuery(String.format(ADD_LANGUAGE_QUERY, languagesTable));
		updateLanguageIndex = st.addQuery(String.format(UPDATE_LANGUAGE_QUERY, languagesTable));
		updateWordIndex = st.addQuery(String.format(UPDATE_WORD_QUERY, wordsTable));
		addWordIndex = st.addQuery(String.format(ADD_WORD_QUERY, wordsTable));

		for(Language language : ld.getWithId())
		{
			String languageId = language.getId();
			SimpleLanguage sl = ld.getById(languageId);

			if(sl == null)
			{
				ld.getWithName().add(language);
			}
			else
			{
				updateLanguageIfUpdated(language, sl);
				Set<String> existedWords = getSetOfWords(language.getWords(), languageId);
				addOrUpdateEachWord(language.getWords(), existedWords, languageId);
			}
		}

		for(Language language : ld.getWithName())
		{
			String name = language.getName();
			SimpleLanguage sl = ld.getByName(name);
			Set<String> existedWords;
			String languageId;
			if(sl!= null)
			{
				languageId = createLanguageIfOldLanguageWasRenamed(language, sl);
				existedWords = getSetOfWords(language.getWords(), sl.id);
			}
			else
			{
				languageId = createLanguage(language);
				existedWords = new HashSet<String>();
			}
			addOrUpdateEachWord(language.getWords(), existedWords, languageId);
		}

		int count = db.processSmdStatement(st);
		return count >= 0;
	}

	private String createLanguageIfOldLanguageWasRenamed(Language language, SimpleLanguage simpleLanguage)
	{
		if(simpleLanguage.name.equals(language.getName()))
			return simpleLanguage.id;

		return createLanguage(language);
	}

	private String createLanguage(Language language)
	{
		if(language.getId() == null)
			language.setId(UUID.randomUUID().toString());//TODO: (3.low)[#26069] use common util for creation Ids.
		st.startSet(addLanguageIndex);
		st.addString(language.getId());
		st.addString(language.getName());
		st.addString(userId);
		st.addLong(currentTime + 1);// Единицу добавляем, чтобы извлечь его при следующем запросе.
		return language.getId();
	}

	private void addOrUpdateEachWord(List<Word> words, Set<String> existedWords, String languageId)
	{
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

	private Set<String> getSetOfWords(List<Word> list, String languageId) throws DbException
	{
		if(list.isEmpty())
		{
			return new HashSet<String>();
		}

		String inList = getInTemplate(list.size());
		String query = String.format(GET_WORDS_IN, wordsTable, inList);

		ISmdStatement wordsStatement = new SmdStatement();
		wordsStatement.addQuery(query);
		wordsStatement.startSet(0);
		wordsStatement.addString(languageId);

		for(Word word : list)
		{
			wordsStatement.addString(word.getOriginal());
		}
		SetParser parser = new SetParser();
		db.select(wordsStatement, parser);
		return parser.set;
	}

	private void updateLanguageIfUpdated(Language language, SimpleLanguage simpleLanguage)
	{
		if(simpleLanguage.name.equals(language.getName()))
			return;

		simpleLanguage.name = language.getName();

		st.startSet(updateLanguageIndex);
		st.addString(language.getName());
		st.addLong(currentTime);
		st.addString(language.getId());
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
}
