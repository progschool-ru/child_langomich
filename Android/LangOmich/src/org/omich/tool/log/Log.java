package org.omich.tool.log;

public class Log 
{
	private static final String TPL_ME = "%1$s; %2$s";
	private static final String TPL_CE = "%1$s: %2$s";
	private static final String TPL_CM = TPL_CE;
	private static final String TPL_CME = "%1$s: %2$s; %2$s";
	
	private static ILog msLog = new StdLog();
	private static ILogMsgCreator msLmc = new StdLogMsgCreator();
	
	public static void init (ILog log)
	{
		msLog = log;
	}

	//==== public Log methods =================================================
	public static void wtf (Class<?> source, String msg, Throwable er)
	{
		log(source, msg, er, Level.WTF);
	}
	
	
	public static void e (Class<?> source, String msg)
	{
		log(source, msg, Level.E);
	}
	
	public static void e (Class<?> source, String msg, Throwable er)
	{
		log(source, msg, er, Level.E);
	}

	public static void e (Class<?> source, String msg, ILoggable er)
	{
		log(source, msg, er, Level.E);
	}

	public static void e (Class<?> source, Throwable er)
	{
		log(source, er, Level.E);
	}

	public static void w (Class<?> source, String msg)
	{
		log(source, msg, Level.W);
	}
	
	public static void w (Class<?> source, ILoggable er)
	{
		log(source, er, Level.W);
	}
	
	public static void w (Class<?> source, Throwable er)
	{
		log(source, er, Level.W);
	}

	public static void w (Class<?> source, String msg, Throwable er)
	{
		log(source, msg, er, Level.W);
	}	

	public static void i (Class<?> source, String msg)
	{
		log(source, msg, Level.I);
	}

	//==== Debug log methods ==================================================	
	@Deprecated
	public static void d (boolean bool)
	{
		d(bool ? "true" : "false" );
	}

	@Deprecated
	public static void d (long msg)
	{
		d(Long.toString(msg));
	}
	
	@Deprecated
	public static void d (double msg)
	{
		d(Double.toString(msg));
	}
	
	@Deprecated
	public static void d (Throwable er)
	{
		log(er, Level.D);
	}
	
	@Deprecated
	public static void d (String msg, Throwable er)
	{
		log(msg, er, Level.D);
	}
	
	@Deprecated
	public static void d (String msg)
	{
		log(msg, Level.D);
	}

//=============================================================================
	private static void log (Throwable er, Level level)
	{
		String m = msLmc.getMsg(er, level);
		msLog.log(m, level);
	}
	
	private static void log (String msg, Level level)
	{
		String m = LogUtil.getNotNullMessage(msg);
		msLog.log(m, level);
	}
	
	private static void log (Class<?> cl, String msg, ILoggable er, Level level)
	{
		msg = LogUtil.getNotNullMessage(msg);
		String m = String.format(TPL_ME, msg, msLmc.getMsg(er, level));
		msLog.log(m, level);
	}

	private static void log (Class<?> cl, ILoggable er, Level level)
	{
		String m = String.format(TPL_CM, getClassMsg(cl), msLmc.getMsg(er, level));
		msLog.log(m, level);
	}
	
	private static void log (Class<?> cl, String msg, Level level)
	{
		msg = LogUtil.getNotNullMessage(msg);
		String m = String.format(TPL_CM, getClassMsg(cl), msg);
		msLog.log(m, level);
	}
	
//	private static void log (Class<?> cl, String msg, ILoggable er, Level level)
//	{
//		msg = LogUtil.getNotNullMessage(msg);
//		String m = String.format(TPL_CME, getClassMsg(cl), msg, msLmc.getMsg(er, level));
//		msLog.log(m, level);
//	}
	
	private static void log (String msg, Throwable er, Level level)
	{
		msg = LogUtil.getNotNullMessage(msg);
		String m = String.format(TPL_ME, msg, msLmc.getMsg(er, level));
		msLog.log(m, level);
	}
	
	private static void log (Class<?> cl, Throwable er, Level level)
	{
		String m = String.format(TPL_CM, getClassMsg(cl), msLmc.getMsg(er, level));
		msLog.log(m, level);
	}
	
	private static void log (Class<?> cl, String msg, Throwable er, Level level)
	{
		msg = LogUtil.getNotNullMessage(msg);
		String m = String.format(TPL_CME, getClassMsg(cl), msg, msLmc.getMsg(er, level));
		msLog.log(m, level);
	}
	
	private static String getClassMsg (Class<?> cl)
	{
		return cl == null ? LogUtil.NULL_STRING_C : cl.getSimpleName();
	}
}
