package org.omich.tool.events;

//import com.ltst.tools.events.IEventDispatcher;

public final class Listeners 
{
	public static interface IListener<Param> 
	{
		void handle (Param value);
	}
	
	public static interface IListenerFloat 
	{
		public void handle (float value);
	}
	
	public static interface IListenerInt 
	{
		public void handle (int value);
	}
	
	public static interface IListenerBoolean
	{
		public void handle (boolean value);
	}
	
	public static interface IListenerVoid
	{
		void handle ();
	}
//	
//	public static interface ITargetListener <Param>
//	{
//		void handle (IEventDispatcher<Param> target, Param value);
//	}
}
