package org.omich.tool.bcops;

import org.omich.tool.events.Listeners.IListenerInt;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class BcTaskHelper
{
	public static boolean isCancelled (ICancelledInfo ci)
	{
		return ci != null && ci.isCancelled();
	}
	
	public static boolean isNetworkAvailable (Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}
	
	public static void handleProgress (IListenerInt ph, int progress)
	{
		if(ph != null)
		{
			ph.handle(progress);
		}
	}
}
