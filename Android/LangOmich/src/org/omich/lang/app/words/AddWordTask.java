package org.omich.lang.app.words;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IWStorage;
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

	private IBcToaster mBcToaster;
	private String mForeign;
	private String mNativ;
	private String mTaskSuccessText;
	private IWStorage mDb;

	public void init(Bundle extras, Context context, IBcToaster bcToaster)
	{
		mBcToaster = bcToaster;
		mForeign = extras.getString(BundleFields.WORD_FOREIGN);
		mNativ = extras.getString(BundleFields.WORD_NATIV);
		mTaskSuccessText = extras.getString(BundleFields.TASK_SUCCESS_TEXT);
		mDb = DbCreator.createWritable(context);
	}

	public Bundle execute(IListenerInt ph, ICancelledInfo ci)
	{
		long wordId = mDb.addWord(mNativ, mForeign);
		
		if(mTaskSuccessText != null && wordId != -1)
		{	
			mBcToaster.showToast(mTaskSuccessText);
		}

		mDb.recycle();
		return null;
	}

}
