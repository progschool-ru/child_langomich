package org.omich.lang.app.db;

public class Separator extends Word
{
	public int rating = 0;
	
	public Separator(int rating)
	{
		super("", "", 0, 0);
		this.rating = rating;
	}
}
