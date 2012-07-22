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
	public long serverId;
	public long time;

	public Dict (long dictId, String name)
	{
		this.dictId = dictId;
		this.name = name;
	}
	public Dict (long dictId, long serverId, String name, long time)
	{
		this.dictId = dictId;
		this.serverId = serverId;
		this.name = name;
		this.time = time;
	}	
	public Dict (Parcel source)
	{
		dictId = source.readLong();
		serverId = source.readLong();
		name = source.readString();
		time = source.readLong();
	}
	
	public int describeContents (){return 0;}

	public void writeToParcel (Parcel dest, int flags)
	{
		dest.writeLong(dictId);
		dest.writeLong(serverId);
		dest.writeString(name);
		dest.writeLong(time);
	}
}
