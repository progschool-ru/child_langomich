package org.omich.lang.app.words;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IWStorage;
import org.omich.tool.bcops.IBcTask;
import android.content.Intent;
import android.os.Bundle;

public class SetNewRatingTask implements IBcTask
{
	public static Intent createIntent (long id ,int rating)
	{
		Intent intent = new Intent();
		intent.putExtra(BundleFields.WORD_ID, id);
		intent.putExtra(BundleFields.WORD_RATING, rating);		
		return intent;
	}
	private long mId;
	private int mRating;
	private IWStorage mDb;
	
	@Override
	public void init(BcTaskEnv env)
	{
		mId = env.extras.getLong(BundleFields.WORD_ID);
		mRating = env.extras.getInt(BundleFields.WORD_RATING);	
		mDb = DbCreator.createWritable(env.context);
	}
	@Override
	public Bundle execute()
	{	
		if(mId != -1)
			mDb.setRating(mId, mRating);			
		mDb.destroy();
		return null;
	}
}