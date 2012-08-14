package org.omich.tool.bcops;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.omich.tool.R;
import org.omich.tool.bcops.IBcTask.BcTaskEnv;
import org.omich.tool.events.Listeners.IListenerInt;
import org.omich.tool.log.Log;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

public class BcService extends IntentService
{
	public static final String BROADCAST_PREFIX = "BcService."; //$NON-NLS-1$
	
	public static final String EVT_PROGRESS = "progress"; //$NON-NLS-1$
	public static final String EVT_CANCEL   = "cancel"; //$NON-NLS-1$
	public static final String EVT_FINISH   = "finish"; //$NON-NLS-1$
	public static final String EVT_START    = "start"; //$NON-NLS-1$
	
	public static final String BF_ERROR            = "BcService.error"; //$NON-NLS-1$
	public static final String BF_EVENT            = "BcService.event"; //$NON-NLS-1$
	public static final String BF_OP_ID            = "BcService.opId"; //$NON-NLS-1$
	public static final String BF_OP_TYPE_ID       = "BcService.opTypeId"; //$NON-NLS-1$
	public static final String BF_PROGRESS_DATA    = "BcService.progressData"; //$NON-NLS-1$
	public static final String BF_RESULT           = "BcService.result"; //$NON-NLS-1$
	public static final String BF_SUCCESS          = "BcService.success"; //$NON-NLS-1$

	private static final String SERVICE_NAME = "BcService"; //$NON-NLS-1$
	private static final BcTypeTaskManager msBcTypeTaskManager = new BcTypeTaskManager();
	
	public static BcTypeTaskManager getTypeTaskManager ()
	{
		return msBcTypeTaskManager;
	}
	
	//========================================================================
	private PH mPh;
	private BR mBr;
	private @Nonnull IBcToaster mBcToaster = IBcToaster.EMPTY_TOASTER;
	
	private String mCurrentOpId;
	private boolean mIsCurrentCancelled;
	private List<String> mCancelledOps = new ArrayList<String>();

	public BcService ()
	{
		super(SERVICE_NAME);
		mPh = new PH(this);
	}
	
	//==== protected interface ===============================================
	protected @Nullable IBcTask getTaskByIntent (@Nonnull Intent intent)
	{
		int opTypeId = intent.getExtras().getInt(BF_OP_TYPE_ID, -1);
		Class<? extends IBcTask> cl = msBcTypeTaskManager.getTypeByIndex(opTypeId);
		
		if(cl == null)
			return null;
		
		try
		{
			IBcTask task = cl.newInstance();
			//task.init(, this, new BcToaster(this, mMainThreadHandler));
			task.init(new BcTaskEnv(
							intent.getExtras(), 
							this, 
							mBcToaster, 
							mPh, 
							mPh
						));
			return task;
		}
		catch (IllegalAccessException e)
		{
			Log.w(getClass(), e);
		}
		catch (InstantiationException e)
		{
			Log.w(getClass(), e);
		}
		return null;
	}

	//==== events ============================================================
	@Override
	public void onCreate ()
	{
		super.onCreate();
		registerLocalReceiver();
		mBcToaster = new BcToaster(this, new Handler());
	}
	
	@Override
	public void onDestroy ()
	{
		unregisterLocalReceiver();
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent (Intent intent)
	{//bg thread
		IBcTask task = null;
		synchronized (this)
		{
			@SuppressWarnings("null")
			@Nonnull String opId = intent.getExtras().getString(BF_OP_ID);
			mCurrentOpId = opId;
			mIsCurrentCancelled = isCancelled(opId);
			if(!mIsCurrentCancelled)
			{
				task = getTaskByIntent(intent);
			}
		}
		sendStartBroadcast();

		Bundle result = null;
		if(task != null)
		{
			try
			{
				result = task.execute();
			}
			catch (ParcException e)
			{
				result = new Bundle();
				result.putParcelable(BF_ERROR, e.createErrorParcelable());
			}
			catch (Throwable e)
			{
				if(!(e instanceof Exception))
				{
					Log.e(getClass(), getString(R.string.error_bcops_BcService_operationError), e);
				}
				result = new Bundle();
				result.putParcelable(BF_ERROR, new ErrorParcelable(e));
			}
		}
		
		sendResultBroadcast(result);
		
		synchronized (this)
		{
			if(mIsCurrentCancelled)
			{
				mIsCurrentCancelled = false;
				@SuppressWarnings("null")
				@Nonnull String opId = mCurrentOpId;
				setOpUncancelled(opId);
			}
			mCurrentOpId = null;
		}
	}
	
	//=========================================================================
	private void sendProgressBroadcast (int progress)
	{
		Intent intent = createBroadcastIntent();
		intent.putExtra(BF_EVENT, EVT_PROGRESS);
		intent.putExtra(BF_PROGRESS_DATA, progress);
		sendLocalBroadcast(intent);
	}
	
	private void sendStartBroadcast ()
	{
		Intent intent = createBroadcastIntent();
		intent.putExtra(BF_EVENT, EVT_START);
		sendLocalBroadcast(intent);
	}

	private void sendResultBroadcast (@Nullable Parcelable result)
	{
		Intent intent = createBroadcastIntent();
		intent.putExtra(BF_EVENT, EVT_FINISH);
		intent.putExtra(BF_RESULT, result);
		sendLocalBroadcast(intent);
	}
	
	private @Nonnull Intent createBroadcastIntent ()
	{
		if(mCurrentOpId == null)
			return new Intent(BROADCAST_PREFIX);

		return new Intent (BROADCAST_PREFIX + mCurrentOpId);
	}
	
	private void sendLocalBroadcast (@Nonnull Intent intent)
	{
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}
	
	private void registerLocalReceiver ()
	{
		mBr = new BR(this);
		LocalBroadcastManager.getInstance(this).registerReceiver(mBr, 
				new IntentFilter(BROADCAST_PREFIX));
	}
	
	private void unregisterLocalReceiver ()
	{
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mBr);
		mBr = null;
	}
	
	private boolean isCancelled (@Nonnull String opId)
	{
		for(String cOpId : mCancelledOps)
		{
			if(cOpId.equals(opId))
				return true;
		}
		return false;
	}
	
	private void setOpCancelled (@Nonnull String opId)
	{
		if(isCancelled(opId))
			return;
		
		mCancelledOps.add(opId);
	}
	
	private void setOpUncancelled (@Nonnull String opId)
	{
		int size = mCancelledOps.size();
		for(int i = 0; i < size; ++i)
		{
			if(mCancelledOps.get(i).equals(opId))
			{
				mCancelledOps.remove(i);
				return;
			}
		}
	}
	
	private static class PH implements IListenerInt, 
			ICancelledInfo
	{
		private BcService mBs;

		public PH (BcService bgs){mBs = bgs;}
		
		//bg thread
		public void handle (int progress){mBs.sendProgressBroadcast(progress);}
		public boolean isCancelled () {return mBs.mIsCurrentCancelled;}
	}
	
	private static class BR extends BroadcastReceiver
	{
		private BcService mBs;
		
		public  BR (BcService bs){mBs = bs;}

		@Override
		public void onReceive (Context context, Intent intent)
		{//main thread
			synchronized (mBs)
			{
				if(context != mBs)
					return;
				
				String event = intent.getExtras().getString(BF_EVENT);
				boolean cancel = EVT_CANCEL.equals(event);
				
				if(!cancel)
					return;
				
				@Nullable String opId = intent.getExtras().getString(BF_OP_ID);
				if(opId != null)
				{
					mBs.setOpCancelled(opId);
					
					if(opId.equals(mBs.mCurrentOpId))
					{
						mBs.mIsCurrentCancelled = true;
					}
				}
			}
		}
	}
}
