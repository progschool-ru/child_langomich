package org.omich.tool.events;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Набор интерфейсов для простого и удобного создания функций обратного вызова (callback).
 */
public final class Listeners 
{
	public static interface IListener<Param> 
	{
		void handle (@Nullable Param value);
	}
	
	public static interface INistener<Param> 
	{
		void handle (@Nonnull Param value);
	}
	
	public static interface IListenerFloat 
	{
		public void handle (float value);
	}
	
	public static interface IListenerDouble 
	{
		public void handle (double value);
	}
	
	public static interface IListenerByte 
	{
		public void handle (byte value);
	}
	
	public static interface IListenerShort
	{
		public void handle (short value);
	}
	
	public static interface IListenerInt 
	{
		public void handle (int value);
	}
	
	public static interface IListenerLong
	{
		public void handle (long value);
	}
	
	public static interface IListenerBoolean
	{
		public void handle (boolean value);
	}
	
	public static interface IListenerChar 
	{
		public void handle (char value);
	}
	
	public static interface IListenerVoid
	{
		void handle ();
	}
}
