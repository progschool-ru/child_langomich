package org.omich.tool.bcops;

import javax.annotation.Nonnull;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class BcToaster implements IBcToaster
{
	private @Nonnull Handler mMainThreadHandler;
	private @Nonnull Context mContext;
	
	public BcToaster (@Nonnull Context context, @Nonnull Handler mth)
	{
		mContext = context;
		mMainThreadHandler = mth;
	}
	
	public void showToast (@Nonnull String msg)
	{
		mMainThreadHandler.post(new BcToasterRunnable(mContext, msg));
	}
	
	private static class BcToasterRunnable implements Runnable
	{
		private @Nonnull String mMsg;
		private @Nonnull Context mContext;

		private BcToasterRunnable (@Nonnull Context context, @Nonnull String msg)
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
