package org.omich.lang.app.db;

import java.util.List;

public interface IRStorage
{
	void destroy();

	List<Word> getWords ();
	List<Dict> getDicts ();
}
