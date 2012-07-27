package org.omich.lang.app.db;

import java.util.List;

public interface IRStorage
{
	void destroy();
	
	List<Word> getWords ();
	List<Word> getWordsByTime (Long mobileTime);
	List<Word> getWordsByDictId (Long dictId);
	Word getRandomWord (Long dictId);
	List<Word> getRandomWords (Long dictId, int n);
	List<Dict> getDicts ();
	List<Dict> getDicts (Long mobileTime);
}
