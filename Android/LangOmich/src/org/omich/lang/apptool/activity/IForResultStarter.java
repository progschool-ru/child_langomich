package org.omich.lang.apptool.activity;

import org.omich.tool.events.Listeners.IListener;

import android.content.Intent;

public interface IForResultStarter
{
	void startForResult(Intent intent, IListener<Intent> handler);
}
