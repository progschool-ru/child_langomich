package org.omich.tool.bcops;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class BcToaster implements IBcToaster
{
	private Handler mMainThreadHandler;
	private Context mContext;
	
	public BcToaster (Context context, Handler mth)
	{
		mContext = context;
		mMainThreadHandler = mth;
	}
	
	public void showToast (String msg)
	{
		mMainThreadHandler.post(new BcToasterRunnable(mContext, msg));
	}
	
	private static class BcToasterRunnable implements Runnable
	{
		private String mMsg;
		private Context mContext;

		private BcToasterRunnable (Context context, String msg)
		{
			mMsg = msg;
			mContext = context;
		}

		public void run ()
		{
			Toast.makeText(mContext, mMsg, Toast.LENGTH_SHORT).show();
		}
	}
}
