package org.omich.lang.app.db;

public interface IWStorage extends IRStorage
{
	long addWord (String nativ, String foreign, long dictId);
	long addWord (String nativ, String foreign, int rating, long dictId);
	long addDict (String name);
	long addDict (String serverId, String name);
	void setRating (long id, int rating);
}
