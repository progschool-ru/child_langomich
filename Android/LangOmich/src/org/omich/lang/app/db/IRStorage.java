package org.omich.lang.app.db;

import java.util.List;

public interface IRStorage
{
	void destroy();
	List<ListItem> getWordsByTime (Long mobileTime);
	List<ListItem> getWordsByDictId (Long dictId);
	List<ListItem> getWordsByDictIdAndText (Long dictId, String text);
	List<ListItem> getRandomWords (Long dictId, int n, int weight[]);
	List<Dict> getDicts ();
	List<Dict> getDicts (Long mobileTime);
}
