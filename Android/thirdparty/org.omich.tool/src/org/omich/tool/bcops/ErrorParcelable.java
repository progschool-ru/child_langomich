package org.omich.tool.bcops;

import javax.annotation.Nonnull;

import org.omich.tool.constants.Strings;
import org.omich.tool.log.ILoggable;
import org.omich.tool.log.LogUtil;

import android.os.Parcel;
import android.os.Parcelable;

public class ErrorParcelable implements Parcelable, ILoggable
{
	public static class TraceElem
	{
		private static final String FMT = "%s (%s:%d)"; //$NON-NLS-1$

		String className;
		String fileName;
		int lineNumber;
		String methodName;

		@Override
		public String toString ()
		{
			return String.format(FMT, methodName, className, lineNumber);
		}
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

	public ErrorParcelable (@Nonnull Throwable error)
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

	//==== ILoggable =========================================================
	public @Nonnull String getShortLogMessage()
	{
		return simpleName + ": " + LogUtil.getNotNullMessage(message); //$NON-NLS-1$
	}

	public @Nonnull String getFullLogMessage()
	{               
		return simpleName + ": "  //$NON-NLS-1$
			+ LogUtil.getNotNullMessage(message) + "\n"  //$NON-NLS-1$
			+ getStackTraceMsg();
	}

	//========================================================================
	private @Nonnull String getStackTraceMsg ()
	{
		if(trace == null)
		return Strings.EMPTY;
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 10 && i < trace.length; ++i)
		{
			sb.append(trace[i].toString());
			sb.append(trace);
		}
		@SuppressWarnings("null")
		@Nonnull String result = sb.toString();
		return result;
	}
}
