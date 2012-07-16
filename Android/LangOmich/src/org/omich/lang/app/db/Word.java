package org.omich.lang.app.db;

import android.os.Parcel;
import android.os.Parcelable;

public class Word implements Parcelable
{
	public static final Parcelable.Creator<Word> CREATOR = new Parcelable.Creator<Word>()
	{
		public Word createFromParcel (Parcel source)	{return new Word(source);}
		public Word[] newArray (int size) {return new Word[0];}
	};

	public String nativ;
	public String foreign;
	public int rating;
	public long id;
	
	public Word (String n, String f, int r){nativ = n; foreign = f; rating = r;}
	public Word (String n, String f, int r, long i){nativ = n; foreign = f; rating = r; id = i;}
	
	public Word (Parcel source)
	{
		nativ = source.readString();
		foreign = source.readString();
		rating = source.readInt();
	}

	public int describeContents (){return 0;}

	public void writeToParcel (Parcel dest, int flags)
	{
		dest.writeString(nativ);
		dest.writeString(foreign);
		dest.writeInt(rating);
	}
}
