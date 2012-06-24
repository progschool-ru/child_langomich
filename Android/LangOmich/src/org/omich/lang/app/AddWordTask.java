package org.omich.lang.app;

import org.omich.lang.app.db.Db;
import org.omich.lang.apptool.events.Listeners.IListenerInt;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.bcops.IBcToaster;
import org.omich.tool.bcops.ICancelledInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AddWordTask implements IBcTask
{
	public static Intent createIntent (String nativ, String foreign,
			String taskSuccessText)
	{
		Intent intent = new Intent();
		intent.putExtra(BundleFields.WORD_NATIV, nativ);
		intent.putExtra(BundleFields.WORD_FOREIGN, foreign);
		intent.putExtra(BundleFields.TASK_SUCCESS_TEXT, taskSuccessText);
		return intent;
	}

//	private Context mContext;
	private IBcToaster mBcToaster;
	private String mForeign;
	private String mNativ;
	private String mTaskSuccessText;
	private Db mDb;

	public void init(Bundle extras, Context context, IBcToaster bcToaster)
	{
//		mContext = context;
		mBcToaster = bcToaster;
		mForeign = extras.getString(BundleFields.WORD_FOREIGN);
		mNativ = extras.getString(BundleFields.WORD_NATIV);
		mTaskSuccessText = extras.getString(BundleFields.TASK_SUCCESS_TEXT);
		mDb = new Db();
	}

	public Bundle execute(IListenerInt ph, ICancelledInfo ci)
	{
		mDb.addWord(mNativ, mForeign, 0);
		
		if(mTaskSuccessText != null)
		{	
			mBcToaster.showToast(mTaskSuccessText);
//			Toast.makeText(mContext.getApplicationContext(), mTaskSuccessText, Toast.LENGTH_SHORT).show();
		}
		return null;
	}

}
