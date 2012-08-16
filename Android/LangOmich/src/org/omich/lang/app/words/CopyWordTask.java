package org.omich.lang.app.words;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IWStorage;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.bcops.IBcToaster;
import android.content.Intent;
import android.os.Bundle;

public class CopyWordTask implements IBcTask
{
	public static Intent createIntent (String nativ, String foreign, int rating, long dictId, String taskSuccessText)
	{
		Intent intent = new Intent();
		intent.putExtra(BundleFields.WORD_NATIV, nativ);
		intent.putExtra(BundleFields.WORD_FOREIGN, foreign);
		intent.putExtra(BundleFields.WORD_RATING, rating);
		intent.putExtra(BundleFields.WORD_DICT_ID, dictId);
		intent.putExtra(BundleFields.TASK_SUCCESS_TEXT, taskSuccessText);
		return intent;
	}

	private IBcToaster mBcToaster;
	private String mNativ;
	private String mForeign;
	private int mRating;
	private long mDictID;
	private String mTaskSuccessText;
	private IWStorage mDb;

	public void init(BcTaskEnv env)
	{
		mBcToaster = env.bcToaster;
		mNativ = env.extras.getString(BundleFields.WORD_NATIV);
		mForeign = env.extras.getString(BundleFields.WORD_FOREIGN);
		mRating = env.extras.getInt(BundleFields.WORD_RATING);
		mDictID = env.extras.getLong(BundleFields.WORD_DICT_ID);
		mTaskSuccessText = env.extras.getString(BundleFields.TASK_SUCCESS_TEXT);
		mDb = DbCreator.createWritable(env.context);
	}

	public Bundle execute()
	{	
		boolean success = mDb.copyWord(mNativ, mForeign, mRating, mDictID);
		if(success && mTaskSuccessText != null)
				mBcToaster.showToast(mTaskSuccessText);		
		mDb.destroy();	
		
		return null;
	}

}