package org.omich.lang.app.db;

import java.util.List;

public interface IRStorage
{
	void recycle();

	List<Word> getWords ();
	List<Dict> getDicts ();
}
