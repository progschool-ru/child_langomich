package org.omich.tool.bcops;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.omich.tool.events.Listeners.IListener;
import org.omich.tool.events.Listeners.IListenerInt;
import org.omich.tool.events.Listeners.IListenerVoid;

import android.content.Intent;
import android.os.Bundle;

public class BcEventHelper
{
	public static void parseEvent (@Nonnull Intent intent, 
			@Nullable IListenerVoid start,
			@Nullable IListenerInt progress,
			@Nullable IListener<Bundle> finish,
			@Nullable IListenerVoid cancel,
			@Nullable IListener<Intent> other)
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
