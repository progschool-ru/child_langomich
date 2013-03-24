package org.omich.lang.app.db;

import android.os.Parcel;
import android.os.Parcelable;

public class ListItem implements Parcelable
{
	public int type;
	public Word word = null;
	public Separator sep = null;
	public ListItem(int rating)
	{
		sep = new Separator(rating);
	}

	public ListItem(Word w)
	{
		word = w;
	}

	public Word getWord()
	{
		return word;
	}
	
	public Separator getSeparator()
	{
		return sep;
	}

	public int describeContents() { return 0;}	

	public void writeToParcel(Parcel dest, int flags) 
	{
		word.writeToParcel(dest, flags);		
	}
}
