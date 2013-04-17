package org.omich.lang.app.words;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IWStorage;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.bcops.IBcToaster;
import android.content.Intent;
import android.os.Bundle;

public class FirstAddWordTask implements IBcTask
{
	public static Intent createIntent (String nativ, String foreign, String dict,
			String taskSuccessText)
	{
		Intent intent = new Intent();
		intent.putExtra(BundleFields.WORD_NATIV, nativ);
		intent.putExtra(BundleFields.WORD_FOREIGN, foreign);
		intent.putExtra(BundleFields.DICT_NAME, dict);
		intent.putExtra(BundleFields.TASK_SUCCESS_TEXT, taskSuccessText);
		return intent;
	}

	private IBcToaster mBcToaster;
	private String mForeign;
	private String mNativ;
	private String mDict;
	private String mTaskSuccessText;
	private IWStorage mDb;

	@Override
	public void init(BcTaskEnv env)
	{
		mBcToaster = env.bcToaster;
		mForeign = env.extras.getString(BundleFields.WORD_FOREIGN);
		mNativ = env.extras.getString(BundleFields.WORD_NATIV);
		mDict = env.extras.getString(BundleFields.DICT_NAME);
		mTaskSuccessText = env.extras.getString(BundleFields.TASK_SUCCESS_TEXT);
		mDb = DbCreator.createWritable(env.context);
	}

	@Override
	public Bundle execute()
	{
		long dictId = mDb.addDict(mDict);
		long wordId = -1;
		if(dictId != -1)
			wordId = mDb.addWord(mNativ, mForeign, dictId);
		
		if(mTaskSuccessText != null && dictId != -1 && wordId != -1)
		{	
			mBcToaster.showToast(mTaskSuccessText);
		}

		mDb.destroy();
		
		Bundle result = new Bundle();
		result.putLong(BundleFields.DICT_ID, dictId);
		return result;
	}
}