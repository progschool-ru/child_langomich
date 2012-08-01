package org.omich.lang.app.db;

import java.util.List;

public interface IRStorage
{
	void destroy();
	List<Word> getWordsByTime (Long mobileTime);
	List<Word> getWordsByDictId (Long dictId);
	List<Word> getRandomWords (Long dictId, int n, int weight[]);
	List<Dict> getDicts ();
	List<Dict> getDicts (Long mobileTime);
}
