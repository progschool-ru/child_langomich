package org.omich.lang.app.words;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IWStorage;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.bcops.IBcToaster;
import android.content.Intent;
import android.os.Bundle;

public class EditWordTask implements IBcTask
{
	public static Intent createIntent (long id, String nativ, String foreign, long dictId, String taskSuccessText)
	{
		Intent intent = new Intent();
		intent.putExtra(BundleFields.WORD_ID, id);
		intent.putExtra(BundleFields.WORD_NATIV, nativ);
		intent.putExtra(BundleFields.WORD_FOREIGN, foreign);
		intent.putExtra(BundleFields.WORD_DICT_ID, dictId);
		intent.putExtra(BundleFields.TASK_SUCCESS_TEXT, taskSuccessText);
		return intent;
	}

	private IBcToaster mBcToaster;
	private long mId;
	private String mNativ;
	private String mForeign;
	private long mDictId;
	private String mTaskSuccessText;
	private IWStorage mDb;

	@Override
	public void init(BcTaskEnv env)
	{
		mBcToaster = env.bcToaster;
		mId = env.extras.getLong(BundleFields.WORD_ID);
		mNativ = env.extras.getString(BundleFields.WORD_NATIV);
		mForeign = env.extras.getString(BundleFields.WORD_FOREIGN);
		mDictId = env.extras.getLong(BundleFields.WORD_DICT_ID);
		mTaskSuccessText = env.extras.getString(BundleFields.TASK_SUCCESS_TEXT);
		mDb = DbCreator.createWritable(env.context);
	}

	@Override
	public Bundle execute()
	{	
		boolean success = mDb.changeWord(mId, mNativ, mForeign, mDictId);
		if(success && mTaskSuccessText != null)
				mBcToaster.showToast(mTaskSuccessText);		
		mDb.destroy();	
		
		return null;
	}
}