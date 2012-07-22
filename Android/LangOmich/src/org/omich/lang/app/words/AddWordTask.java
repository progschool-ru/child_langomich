package org.omich.lang.app.words;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IWStorage;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.bcops.IBcToaster;
import android.content.Intent;
import android.os.Bundle;

public class AddWordTask implements IBcTask
{
	public static Intent createIntent (String nativ, String foreign, long dictId,
			String taskSuccessText)
	{
		Intent intent = new Intent();
		intent.putExtra(BundleFields.WORD_NATIV, nativ);
		intent.putExtra(BundleFields.WORD_FOREIGN, foreign);
		intent.putExtra(BundleFields.WORD_DICT_ID, dictId);
		intent.putExtra(BundleFields.TASK_SUCCESS_TEXT, taskSuccessText);
		return intent;
	}

	private IBcToaster mBcToaster;
	private String mForeign;
	private String mNativ;
	private long mDictID;
	private String mTaskSuccessText;
	private IWStorage mDb;

	public void init(BcTaskEnv env)
	{
		mBcToaster = env.bcToaster;
		mForeign = env.extras.getString(BundleFields.WORD_FOREIGN);
		mNativ = env.extras.getString(BundleFields.WORD_NATIV);
		mDictID = env.extras.getLong(BundleFields.WORD_DICT_ID);
		mTaskSuccessText = env.extras.getString(BundleFields.TASK_SUCCESS_TEXT);
		mDb = DbCreator.createWritable(env.context);
	}

	public Bundle execute()
	{
		long wordId = mDb.addWord(mNativ, mForeign, mDictID);
		
		if(mTaskSuccessText != null && wordId != -1)
		{	
			mBcToaster.showToast(mTaskSuccessText);
		}

		mDb.destroy();
		return null;
	}

}
