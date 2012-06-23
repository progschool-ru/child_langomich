package org.omich.lang.apptool.log;

public class StdLog implements ILog
{
	private String mLogId;
	
	public StdLog (){this(StdLog.class.getName());}
	public StdLog (String logId){mLogId = logId;}
	
	public void log (String msg, Level level)
	{
		switch(level)
		{
		case WTF:
			android.util.Log.wtf(mLogId, msg);
			break;
		case E:
			android.util.Log.e(mLogId, msg);
			break;
		case W:
			android.util.Log.w(mLogId, msg);
			break;
		case I:
			android.util.Log.i(mLogId, msg);
			break;
		case D:
			android.util.Log.d(mLogId, msg);
			break;
		}
	}
}
