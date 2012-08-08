package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.apptool.activity.BcActivity;
import org.omich.tool.events.Listeners.IListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

public class EditWordActivity extends BcActivity
{
	private String mEditWordTaskId;
	private boolean mIsDestroyed;
	
	private String nativ;
	private String foreign;
	private long id;

	//==== live cycle =========================================================
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		id = getIntent().getExtras().getLong("id");
		nativ = getIntent().getExtras().getString("nativ");
		foreign = getIntent().getExtras().getString("foreign");
		setContentView(R.layout.app_dialog_editword);
		((EditText)findViewById(R.id.editword_edit_nativ)).setText(nativ);
		((EditText)findViewById(R.id.editword_edit_foreign)).setText(foreign);		
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
	@Override
	protected void onDestroy ()
	{
		if(mEditWordTaskId != null)
		{
			getBcConnector().unsubscribeTask(mEditWordTaskId);
			mEditWordTaskId = null;
		}	
		mIsDestroyed = true;
		super.onDestroy();
	}
	//==== events =============================================================	
	public void onChangeButton (View v)
	{
		if(mEditWordTaskId != null)
			return;

		String newNativ = ((EditText)findViewById(R.id.editword_edit_nativ)).getText().toString();
		String newForeign = ((EditText)findViewById(R.id.editword_edit_foreign)).getText().toString();
		String taskAddText = getResources().getString(R.string.editword_report_changed);

		if(newNativ.equals("") || newForeign.equals(""))
		{
			TextView errorView = (TextView) findViewById(R.id.editword_errorReport);
			errorView.setTextColor(Color.RED);
			errorView.setText(R.string.editword_text_empty);
		}
		else
		{
			Intent intent = EditWordTask.createIntent(id, newNativ, newForeign, taskAddText);
			mEditWordTaskId = getBcConnector().startTypicalTask(EditWordTask.class, intent, new IListener<Bundle>()
				{
					public void handle (Bundle bundle)
					{
						if(mIsDestroyed)
							return;
		
						mEditWordTaskId = null;
						
						boolean success = true;
						if(success)
						{
							Intent intent = new Intent();
	        		    	intent.putExtra("result", true);
	        		    	setResult(RESULT_OK, intent);
						}
						finish();
					}
				});		
		}
	}
	public void onCancelButton (View v)
	{			
		finish();
	}		
}
