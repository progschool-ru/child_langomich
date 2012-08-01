package org.omich.lang.app.words;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IWStorage;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.bcops.IBcToaster;
import android.content.Intent;
import android.os.Bundle;

public class SetNewRatingTask implements IBcTask
{
	public static Intent createIntent (long id ,int rating, String taskSuccessText)
	{
		Intent intent = new Intent();
		intent.putExtra(BundleFields.WORD_ID, id);
		intent.putExtra(BundleFields.WORD_RATING, rating);		
		intent.putExtra(BundleFields.TASK_SUCCESS_TEXT, taskSuccessText);
		return intent;
	}
	private IBcToaster mBcToaster;	
	private long mId;
	private int mRating;
	private String mTaskSuccessText;
	private IWStorage mDb;
	
	public void init(BcTaskEnv env)
	{
		mBcToaster = env.bcToaster;
		mId = env.extras.getLong(BundleFields.WORD_ID);
		mRating = env.extras.getInt(BundleFields.WORD_RATING);	
		mTaskSuccessText = env.extras.getString(BundleFields.TASK_SUCCESS_TEXT);
		mDb = DbCreator.createWritable(env.context);
	}
	public Bundle execute()
	{
		boolean success = false;	
		if(mId != -1)
			success = mDb.setRating(mId, mRating);			
		if(success && mTaskSuccessText != null)
		{	
			mBcToaster.showToast(mTaskSuccessText+" - "+mRating);
		}
		mDb.destroy();
		return null;
	}
}