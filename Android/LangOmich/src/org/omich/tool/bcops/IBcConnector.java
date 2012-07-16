package org.omich.tool.bcops;

import org.omich.lang.apptool.events.Listeners.IListener;

import android.content.Intent;
import android.os.Bundle;

public interface IBcConnector
{
	public String startTask (Class<? extends BcService> serviceClass,
			Class<? extends IBcTask> taskClass,
			Intent intent,
			IListener<Intent> handler);
	public String startTypicalTask (Class<? extends IBcTask> taskClass,
			Intent intent,
			IListener<Bundle> finishHandler);
	public void cancelTask (String opId);
	public void unsubscribeTask (String opId);
}
