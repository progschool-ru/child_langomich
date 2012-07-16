package org.omich.lang.app.db;

public interface IWStorage extends IRStorage
{
	long addWord (String nativ, String foreign);
	void setRating (long id, int rating);
}
