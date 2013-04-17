package org.omich.tool.log;

public class StdLogMsgCreator implements ILogMsgCreator
{
	@Override
	public String getMsg (Throwable er, Level level)
	{
		return getMsg (new Lgbl(er), level);
	}

	@Override
	public String getMsg (ILoggable er, Level level)
	{
		if(er == null)
		{
			return LogUtil.NULL_STRING_E;
		}

		switch(level)
		{
			case WTF:
			case E:
				return er.getFullLogMessage();
			case W:
			case I:
			case D:
			default:
				return er.getShortLogMessage();
		}
	}
	
	private static class Lgbl implements ILoggable
	{
		private Throwable mEr;
		public Lgbl (Throwable er) {mEr = er;}
		@Override
		public String getShortLogMessage (){return LogUtil.getShortLogMessage(mEr);}
		@Override
		public String getFullLogMessage (){return LogUtil.getFullLogMessage(mEr);}
	}
}
