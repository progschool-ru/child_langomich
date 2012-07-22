package org.omich.lang.app.db;

import java.util.List;

public interface IWStorage extends IRStorage
{
	long addWord (String nativ, String foreign, long dictId);
	long addDict (String name);
	void addWords (List<Word> words);
	void addDicts (List<Dict> dicts);	
	void setRating (long id, int rating);
}
