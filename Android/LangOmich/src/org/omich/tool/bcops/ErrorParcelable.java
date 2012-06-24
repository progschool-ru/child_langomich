package org.omich.tool.bcops;

import android.os.Parcel;
import android.os.Parcelable;

public class ErrorParcelable implements Parcelable
{
	public static class TraceElem
	{
		String className;
		String fileName;
		int lineNumber;
		String methodName;
	}
	
	public static final Parcelable.Creator<ErrorParcelable> CREATOR
			= new Parcelable.Creator<ErrorParcelable>()
			{
				public ErrorParcelable createFromParcel(Parcel source)
				{
					return new ErrorParcelable(source);
				}

				public ErrorParcelable[] newArray(int size)
				{
					return new ErrorParcelable[size];
				}
			};

	//========================================================================
	public String className;
	public String simpleName;
	public String message;
	public String type;
	public TraceElem[] trace;

	public ErrorParcelable (Throwable error)
	{
		className = error.getClass().getCanonicalName();
		simpleName = error.getClass().getSimpleName();
		message = error.getMessage();
		
		StackTraceElement[] tr = error.getStackTrace();
		trace = new TraceElem[tr.length];

		for(int i = 0; i < tr.length; ++i)
		{
			StackTraceElement el = tr[i];
			TraceElem elem = new TraceElem();
			elem.className = el.getClassName();
			elem.fileName = el.getFileName();
			elem.lineNumber = el.getLineNumber();
			elem.methodName = el.getMethodName();
			trace[i] = elem;
		}
	}
	
	public ErrorParcelable (Parcel source)
	{
		className = source.readString();
		simpleName = source.readString();
		message = source.readString();
		type = source.readString();

		trace = new TraceElem[source.readInt()];

		for(int i = 0; i < trace.length; ++i)
		{
			TraceElem elem = new TraceElem();
			elem.className = source.readString();
			elem.fileName = source.readString();
			elem.lineNumber = source.readInt();
			elem.methodName = source.readString();
			trace[i] = elem;
		}		
	}

	//==== Parcelable =========================================================
	public int describeContents (){return 0;}

	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(className);
		dest.writeString(simpleName);
		dest.writeString(message);
		dest.writeString(type);
		dest.writeInt(trace.length);
		for(int i = 0; i < trace.length; ++i)
		{
			TraceElem te = trace[i];
			dest.writeString(te.className);
			dest.writeString(te.fileName);
			dest.writeInt(te.lineNumber);
			dest.writeString(te.methodName);
		}
	}
}