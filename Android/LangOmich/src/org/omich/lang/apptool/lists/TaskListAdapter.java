package org.omich.lang.apptool.lists;

import java.util.List;

import org.omich.lang.apptool.events.Listeners.IListener;
import org.omich.tool.bcops.BcEventHelper;
import org.omich.tool.bcops.BcService;
import org.omich.tool.bcops.IBcConnector;
import org.omich.tool.bcops.IBcTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

abstract public class TaskListAdapter<Item extends Parcelable> extends ListAdapter<Item>
{
	private IBcConnector mConn;
	private String mLoadItemsTaskId;

	protected TaskListAdapter(Context context, IBcConnector conn)
	{
		super(context);
		mConn = conn;
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
		conn.startTask(BcService.class, getLoadItemsTaskClass(), 
				createLoadItemsIntent(), new IListener<Intent>()
				{
					public void handle(Intent value)
					{
						BcEventHelper.parseEvent(value, null, null, new IListener<Bundle>()
						{
							public void handle (Bundle b)
							{
								mLoadItemsTaskId = null;
			
								List<Item> dicts = b.<Item>getParcelableArrayList(getListBundleField());							
								setItems(dicts);
							}
						}, null, null);
					}
				});
	}
}
