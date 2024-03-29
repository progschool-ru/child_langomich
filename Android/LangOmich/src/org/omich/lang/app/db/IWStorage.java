package org.omich.lang.app.db;

public interface IWStorage extends IRStorage
{
	long addWord (String nativ, String foreign, long dictId);	
	long addWord (String nativ, String foreign, int rating, long dictId);
	long addDict (String name);
	boolean setRating (long id, int rating);
	boolean deleteWord (long id);
	boolean changeWord (long id, String nativ, String foreign, long dictId);
}
