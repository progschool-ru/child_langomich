package org.omich.lang.app.db;

import android.os.Parcel;
import android.os.Parcelable;

public class Dict implements Parcelable
{
	public static final Parcelable.Creator<Dict> CREATOR = new Parcelable.Creator<Dict>()
	{
		@Override
		public Dict createFromParcel (Parcel source)	{return new Dict(source);}
		@Override
		public Dict[] newArray (int size) {return new Dict[0];}
	};
	
	//========================================================================
	public long dictId;
	public String name;
	public long time;

	public Dict (long dictId, String name)
	{
		this.dictId = dictId;
		this.name = name;
	}		
	
	public Dict (Parcel source)
	{
		dictId = source.readLong();
		name = source.readString();
		time = source.readLong();
	}
	
	@Override
	public int describeContents (){return 0;}

	@Override
	public void writeToParcel (Parcel dest, int flags)
	{
		dest.writeLong(dictId);
		dest.writeString(name);
		dest.writeLong(time);
	}
}
