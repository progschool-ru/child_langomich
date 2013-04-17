package org.omich.tool.bcops;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.omich.tool.events.Listeners.IListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public class BcConnector implements IBcConnector
{
	private Context mContext;
	private Map<String, TaskReceiver> mMap = new HashMap<String, TaskReceiver>();

	public BcConnector (Context context) {mContext = context;}

	public void destroy ()
	{
		LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(mContext);
		Set<Map.Entry<String, TaskReceiver>> set = mMap.entrySet();

		for(Map.Entry<String, TaskReceiver> entry : set)
		{
			lbm.unregisterReceiver(entry.getValue());
			entry.getValue().mClosed = true;
		}

		mMap = null;
		mContext = null;
	}

	//==== IBcConnector =======================================================
	@Override
	public String startTask (Class<? extends BcService> serviceClass, 
			Class<? extends IBcTask> taskClass,
			Intent intent,
			IListener<Intent> handler)
	{
		int opTypeId = BcService.getTypeTaskManager().registerType(taskClass);
		
		String opId = UUID.randomUUID().toString();
		subscribe(opId, handler);

		intent.setClass(mContext, serviceClass);
		intent.putExtra(BcService.BF_OP_ID, opId);
		intent.putExtra(BcService.BF_OP_TYPE_ID, opTypeId);
		mContext.startService(intent);
		
		return opId;
	}
	
	@Override
	public String startTypicalTask (Class<? extends IBcTask> taskClass,
			Intent intent,
			IListener<Bundle> finishHandler)
	{
		return startTask (BcService.class, taskClass, intent, new TypicalTaskHandler(finishHandler));
	}
	
	@Override
	public void cancelTask (String opId)
	{
		Intent intent = new Intent(BcService.BROADCAST_PREFIX);
		intent.putExtra(BcService.BF_EVENT, BcService.EVT_CANCEL);
		intent.putExtra(BcService.BF_OP_ID, opId);
		LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
	}
	
	@Override
	public void unsubscribeTask (String opId)
	{
		TaskReceiver tr = mMap.get(opId);
		LocalBroadcastManager.getInstance(mContext).unregisterReceiver(tr);
		tr.mClosed = true;
		mMap.remove(opId);
	}
	
	//========================================================================
	private void subscribe (String opId, IListener<Intent> handler)
	{
		if(handler != null)
		{
			TaskReceiver tr = new TaskReceiver(this, handler, opId);
			LocalBroadcastManager.getInstance(mContext)
				.registerReceiver(tr, new IntentFilter(BcService.BROADCAST_PREFIX + opId));
			mMap.put(opId, tr);
		}
	}
	
	private static class TaskReceiver extends BroadcastReceiver
	{
		private BcConnector mBc;
		private boolean mClosed;
		private IListener<Intent> mHandler;
		private String mOpId;
		
		public TaskReceiver (BcConnector bc, IListener<Intent> handler,
				String opId)
		{
			mBc = bc;
			mHandler = handler;
			mOpId = opId;
		}

		@Override
		public void onReceive (Context context, Intent intent)
		{//main thread
			if(mClosed)
				return;
			
			String event = intent.getExtras().getString(BcService.BF_EVENT);
			if(BcService.EVT_FINISH.equals(event))
			{
				LocalBroadcastManager.getInstance(mBc.mContext).unregisterReceiver(this);
				mBc.mMap.remove(mOpId);
				mClosed = true;
			}
			
			mHandler.handle(intent);
		}
	}
	
	private static class TypicalTaskHandler implements IListener<Intent>
	{
		private IListener<Bundle> mFinishHandler;
		
		public TypicalTaskHandler (IListener<Bundle> finishHandler)
		{
			mFinishHandler = finishHandler;
		}

		@Override
		public void handle (Intent intent)
		{
			BcEventHelper.parseEvent(intent, null, null, mFinishHandler, null, null);
		}
	}
}
