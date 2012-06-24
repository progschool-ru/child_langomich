package org.omich.lang.app;

import org.omich.lang.R;
import org.omich.lang.apptool.activity.BcActivity;
import org.omich.lang.apptool.events.Listeners.IListener;
import org.omich.tool.bcops.BcEventHelper;
import org.omich.tool.bcops.BcService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

public class AddWordActivity extends BcActivity
{
	private String mAddWordTaskId;
	
	private boolean mIsDestroyed;

	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_dialog_addword);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
	
	@Override
	protected void onDestroy ()
	{
		if(mAddWordTaskId != null)
		{
			getBcConnector().unsubscribeTask(mAddWordTaskId);
			mAddWordTaskId = null;
		}
		mIsDestroyed = true;
		super.onDestroy();
	}
	
	//==== events =============================================================
	public void onAddButton (View v)
	{
		if(mAddWordTaskId != null)
			return;

		String nativ = ((EditText)findViewById(R.id.addword_nativEdit)).getText().toString();
		String foreign = ((EditText)findViewById(R.id.addword_foreignEdit)).getText().toString();
		String toast = getResources().getString(R.string.dialogAddWordMain_Added);

		Intent intent = AddWordTask.createIntent(nativ, foreign, toast);
		mAddWordTaskId = getBcConnector().startTask(BcService.class, AddWordTask.class, intent,
				new IListener<Intent>()
				{
					public void handle (Intent intent)
					{
						if(mIsDestroyed)
							return;

						BcEventHelper.parseEvent(intent, null, null, new IListener<Bundle>()
						{
							public void handle (Bundle bundle){mAddWordTaskId = null;}
						}, null, null);
					}
				});
		finish();
	}
}