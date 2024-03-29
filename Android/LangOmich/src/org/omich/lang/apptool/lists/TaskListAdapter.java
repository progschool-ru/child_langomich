package org.omich.lang.apptool.lists;

import java.util.List;

import org.omich.tool.bcops.IBcConnector;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.events.Listeners.IListener;
import org.omich.tool.events.Listeners.IListenerVoid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

abstract public class TaskListAdapter<Item extends Parcelable> extends ListAdapter<Item>
{
	private IBcConnector mConn;
	private String mLoadItemsTaskId;
	private IListenerVoid lv;
	protected TaskListAdapter(Context context, IBcConnector conn)
	{
		super(context);
		mConn = conn;
	}
	protected TaskListAdapter(Context context, IBcConnector conn, IListenerVoid lv)
	{
		super(context);
		this.lv = lv;
		mConn = conn;
	}	
	@Override
	public void destroy ()
	{
		if(mLoadItemsTaskId != null)
		{
			mConn.unsubscribeTask(mLoadItemsTaskId);
			mConn.cancelTask(mLoadItemsTaskId);
			mLoadItemsTaskId = null;
		}
		mConn = null;
		super.destroy();
	}
	
	//==== protected interface ===============================================
	abstract protected Class<? extends IBcTask> getLoadItemsTaskClass ();
	abstract protected Intent createLoadItemsIntent ();
	abstract protected String getListBundleField ();
	
	//==== ListAdapter ========================================================
	@Override
	public void reloadItems ()
	{
		if(mLoadItemsTaskId != null)
			return;

		IBcConnector conn = mConn;
		mLoadItemsTaskId = conn.startTypicalTask(getLoadItemsTaskClass(), createLoadItemsIntent(), new IListener<Bundle>()
		{
			@Override
			public void handle (Bundle b)
			{
				mLoadItemsTaskId = null;

				List<Item> dicts = b.<Item>getParcelableArrayList(getListBundleField());	
				setItems(dicts);
				if(lv != null){lv.handle();}
			}
		});
	}
}
