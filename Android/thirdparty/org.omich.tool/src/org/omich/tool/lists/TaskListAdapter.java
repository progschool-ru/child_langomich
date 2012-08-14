package org.omich.tool.lists;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.omich.tool.bcops.IBcConnector;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.events.Listeners.IListener;
import org.omich.tool.events.Listeners.IListenerBoolean;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

/**
 * Наследник от org.omich.tool.lists.ListAdapter
 * 
 * Предусматривает загрузку списка элементов через IBcConnector посредством IBcTask.
 * 
 *  
 * Методы для переопределения:
 * 
 * 	- Class<? extends IBcTask> getLoadItemsTaskClass ();
 *	- Intent createLoadItemsIntent ();
 *	- String getListBundleField ();
 * 
 * @param <Item> - тип элемента списка.
 */
abstract public class TaskListAdapter<Item extends Parcelable> extends OmListAdapter<Item>
{
	private @Nonnull IBcConnector mConn;
	private @Nullable IListenerBoolean mReloadedListener;

	private String mLoadItemsTaskId;
	
	private boolean mIsDestroyed;

	protected TaskListAdapter(@Nonnull Context context, @Nonnull IBcConnector conn)
	{
		super(context);
		mConn = conn;
	}

	protected TaskListAdapter(@Nonnull Context context, @Nonnull IBcConnector conn, 
			@Nullable IListenerBoolean itemsReloadedListener)
	{
		super(context);
		this.mReloadedListener = itemsReloadedListener;
		mConn = conn;
	}

	@Override
	public void destroy ()
	{
		if(mLoadItemsTaskId != null)
		{
			@Nonnull String taskId = mLoadItemsTaskId;
			mConn.unsubscribeTask(taskId);
			mConn.cancelTask(taskId);
			mLoadItemsTaskId = null;
		}

		mIsDestroyed = true;
		super.destroy();
	}
	
	//==== protected interface ===============================================
	abstract protected @Nonnull Class<? extends IBcTask> getLoadItemsTaskClass ();
	abstract protected @Nonnull Intent createLoadItemsIntent ();
	abstract protected @Nonnull String getListBundleField ();
	
	/*
	 * Защищённый, чтобы наследники могли его переопределить, если им нужно что-то делать по перезагрузке списка.
	 */
	private void handleReloaded ()
	{
		if(mReloadedListener != null)
		{
			mReloadedListener.handle(true);
		}		
	}
	
	//==== ListAdapter ========================================================
	@Override
	public void reloadItems ()
	{
		if(mLoadItemsTaskId != null)
			return;

		mLoadItemsTaskId = mConn.startTypicalTask(getLoadItemsTaskClass(), createLoadItemsIntent(), new IListener<Bundle>()
						{
							public void handle (@Nullable Bundle b)
							{
								if(mIsDestroyed || b == null)
									return;

								mLoadItemsTaskId = null;
			
								List<Item> items = b.<Item>getParcelableArrayList(getListBundleField());
								
								if(items == null)
								{
									items = new ArrayList<Item>();
								}

								setItems(items);
								handleReloaded();
							}
						});
	}
}
