package org.omich.lang.apptool.log;

public interface ILogMsgCreator
{
	String getMsg (Throwable er, Level level);
	String getMsg (ILoggable er, Level level);
}
