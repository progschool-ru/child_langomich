package org.omich.lang.app.db;

import android.os.Parcel;
import android.os.Parcelable;

public class Dict implements Parcelable
{
	public static final Parcelable.Creator<Dict> CREATOR = new Parcelable.Creator<Dict>()
	{
		public Dict createFromParcel (Parcel source)	{return new Dict(source);}
		public Dict[] newArray (int size) {return new Dict[0];}
	};
	
	//========================================================================
	public long dictId;
	public String name;

	public Dict (long dictId, String name)
	{
		this.dictId = dictId;
		this.name = name;
	}
	
	public Dict (Parcel source)
	{
		dictId = source.readLong();
		name = source.readString();
	}
	
	public int describeContents (){return 0;}

	public void writeToParcel (Parcel dest, int flags)
	{
		dest.writeLong(dictId);
		dest.writeString(name);
		
	}
}
