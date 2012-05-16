package org.omich.lang.SQLite;

import java.util.List;

import org.omich.lang.words.Language;

public interface IWordsStorage {
	
	public static final String WORDS_TABLE = "words";
	
	public void open();
	public void close();
	
	public List<Language> getLatestUserWords(long lastSync);
	public List<Language> getLanguages();
	public IQueryLanguage getQueryLanguage(int languageId);
	public long create(Language language);
	public void synchronization(List<Language> languages);
}
