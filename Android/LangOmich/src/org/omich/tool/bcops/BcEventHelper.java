package org.omich.tool.bcops;

import org.omich.lang.apptool.events.Listeners.IListener;
import org.omich.lang.apptool.events.Listeners.IListenerInt;
import org.omich.lang.apptool.events.Listeners.IListenerVoid;

import android.content.Intent;
import android.os.Bundle;

public class BcEventHelper
{
	public static void parseEvent (Intent intent, IListenerVoid start, IListenerInt progress,
			IListener<Bundle> finish, IListenerVoid cancel, IListener<Intent> other)
	{
		Bundle extras = intent.getExtras();
		
		String event = extras.getString(BcService.BF_EVENT);
		
		if(BcService.EVT_START.equals(event))
		{
			if(start != null)
			{
				start.handle();
			}
		}
		else if(BcService.EVT_PROGRESS.equals(event))
		{
			if(progress != null)
			{
				progress.handle(extras.getInt(BcService.BF_PROGRESS_DATA));
			}
		}
		else if(BcService.EVT_FINISH.equals(event))
		{
			if(finish != null)
			{
				finish.handle(extras.getBundle(BcService.BF_RESULT));
			}
		}
		else if(BcService.EVT_CANCEL.equals(event))
		{
			if(cancel != null)
			{
				cancel.handle();
			}
		}
		else
		{
			if(other != null)
			{
				other.handle(intent);
			}
		}
	}
}
