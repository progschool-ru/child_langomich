package org.omich.lang.app;

import org.omich.lang.R;
import org.omich.lang.apptool.activity.AppActivity;
import org.omich.lang.apptool.events.Listeners.IListener;
import org.omich.tool.log.Log;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TempActivity extends AppActivity
{
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_temp);
	}
	//==== events =============================================================
	public void onAddWord (View v)
	{
		getForResultStarter().startForResult(new Intent(this, AddWordActivity.class),
				new IListener<Intent>()
				{
					public void handle(Intent value)
					{
						Log.d("Addded?");
					}
				});
	}
}
