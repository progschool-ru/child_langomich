package org.omich.tool.bcops;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

	public BcConnector (@Nonnull Context context) {mContext = context;}

	public void destroy ()
	{
		LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(mContext);
		Set<Map.Entry<String, TaskReceiver>> set = mMap.entrySet();

		for(Map.Entry<String, TaskReceiver> entry : set)
		{
			lbm.unregisterReceiver(entry.getValue());
			entry.getValue().closed = true;
		}

		mMap = null;
		mContext = null;
	}

	//==== IBcConnector =======================================================
	public @Nonnull String startTask (@Nonnull Class<? extends BcService> serviceClass, 
								@Nonnull Class<? extends IBcTask> taskClass,
								@Nonnull Intent intent,
								@Nullable IListener<Intent> handler)
	{
		int opTypeId = BcService.getTypeTaskManager().registerType(taskClass);
		
		@SuppressWarnings("null")
		@Nonnull String opId = UUID.randomUUID().toString();
		subscribe(opId, handler);

		intent.setClass(mContext, serviceClass);
		intent.putExtra(BcService.BF_OP_ID, opId);
		intent.putExtra(BcService.BF_OP_TYPE_ID, opTypeId);
		mContext.startService(intent);
		
		return opId;
	}
	
	public @Nonnull String startTypicalTask (@Nonnull Class<? extends IBcTask> taskClass,
										@Nonnull Intent intent,
										@Nullable IListener<Bundle> finishHandler)
	{
		return startTask (BcService.class, taskClass, intent, new TypicalTaskHandler(finishHandler));
	}
	
	public void cancelTask (@Nonnull String opId)
	{
		Intent intent = new Intent(BcService.BROADCAST_PREFIX);
		intent.putExtra(BcService.BF_EVENT, BcService.EVT_CANCEL);
		intent.putExtra(BcService.BF_OP_ID, opId);
		LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
	}
	
	public void unsubscribeTask (@Nonnull String opId)
	{
		TaskReceiver tr = mMap.get(opId);
		LocalBroadcastManager.getInstance(mContext).unregisterReceiver(tr);
		tr.closed = true;
		mMap.remove(opId);
	}
	
	//========================================================================
	private void subscribe (@Nonnull String opId, @Nullable IListener<Intent> handler)
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
		public boolean closed;

		private @Nonnull BcConnector mBc;
		private @Nonnull IListener<Intent> mHandler;
		private @Nonnull String mOpId;
		
		public TaskReceiver (@Nonnull BcConnector bc, 
								@Nonnull IListener<Intent> handler,
								@Nonnull String opId)
		{
			mBc = bc;
			mHandler = handler;
			mOpId = opId;
		}

		@Override
		public void onReceive (Context context, Intent intent)
		{//main thread
			if(closed)
				return;
			
			String event = intent.getExtras().getString(BcService.BF_EVENT);
			if(BcService.EVT_FINISH.equals(event))
			{
				LocalBroadcastManager.getInstance(mBc.mContext).unregisterReceiver(this);
				mBc.mMap.remove(mOpId);
				closed = true;
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

		public void handle (@Nullable Intent intent)
		{
			if(intent != null)
			{
				BcEventHelper.parseEvent(intent, null, null, mFinishHandler, null, null);
			}
		}
	}
}
