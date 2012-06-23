package org.omich.lang.apptool.activity;

import java.util.ArrayList;
import java.util.List;

import org.omich.lang.apptool.events.Listeners.IListener;

import android.app.Activity;
import android.content.Intent;

public class ForResultStarter implements IForResultStarter
{
	private static Activity mActivity;
	private List<IListener<Intent>> mHandlers = new ArrayList<IListener<Intent>>();
	
	public ForResultStarter (Activity activity)
	{
		mActivity = activity;
	}
	
	public void recycle ()
	{
		//Благодаря методу recycle мы избавляем себя от риска, 
		// вызвать что-то в Activity, после того, как нас попросили отключиться.
		mHandlers = null;
		mActivity = null;
	}

	public boolean onActivityResult (int reqCode, int resCode, Intent data)
	{
		if(mHandlers == null)
			return false;

		if(reqCode >= 0 && reqCode < mHandlers.size() && mHandlers.get(reqCode) != null)
		{
			mHandlers.get(reqCode).handle(data);
			mHandlers.set(reqCode, null);
			return true;
		}
		return false;
	}
	
	//==== IForResultStarter ==================================================
	public void startForResult(Intent intent, IListener<Intent> handler)
	{
		if(mActivity == null)
			return;

		int reqCode = -1;

		int size = mHandlers.size();
		for(int i = 0; i < size; ++i)
		{
			if(mHandlers.get(i) == null)
			{
				mHandlers.set(i, handler);
				reqCode = i;
				break;
			}
		}
		if(reqCode == -1)
		{
			reqCode = size;
			mHandlers.add(handler);
		}

		mActivity.startActivityForResult(intent, reqCode);
	}
}
