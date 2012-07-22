package org.omich.lang.app.db;

import java.util.List;

public interface IRStorage
{
	void destroy();
	
	List<Word> getWords ();
	List<Word> getWords (Long mobileTime);
	Word getRandomWord ();
	List<Dict> getDicts ();
	List<Dict> getDicts (Long mobileTime);
}
