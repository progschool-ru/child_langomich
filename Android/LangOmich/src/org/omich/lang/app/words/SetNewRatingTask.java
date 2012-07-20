
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
		public static Intent createIntent (long id ,int rating)
		{
			Intent intent = new Intent();
			intent.putExtra(BundleFields.WORD_RATING, rating);
			intent.putExtra(BundleFields.WORD_ID, id);
			return intent;
		}
		private IBcToaster mBcToaster;
		private String mTaskSuccessText;
		private int mRating;
		private long mId;
		private IWStorage mDb;

		public void init(BcTaskEnv env)
		{
			mBcToaster = env.bcToaster;
			mRating = env.extras.getInt(BundleFields.WORD_RATING);
			mId = env.extras.getLong(BundleFields.WORD_ID);
			mTaskSuccessText = env.extras.getString(BundleFields.TASK_SUCCESS_TEXT);
			mDb = DbCreator.createWritable(env.context);
		}

		public Bundle execute()
		{
			if(mId != -1)
				mDb.setRating(mId, mRating);			
			if(mTaskSuccessText != null && mId != -1)
			{	
				mBcToaster.showToast(mTaskSuccessText);
			}
			mDb.destroy();
			return null;
		}
	}