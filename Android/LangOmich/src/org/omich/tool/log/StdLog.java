package org.omich.tool.log;

public class StdLog implements ILog
{
	public static final String LOG_ID = StdLog.class.getName();
	private String mLogId;
	
	public StdLog (){this(LOG_ID);}
	public StdLog (String logId){mLogId = logId;}
	
	@Override
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
