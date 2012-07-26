package org.omich.lang.app.words;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IWStorage;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.bcops.IBcToaster;
import android.content.Intent;
import android.os.Bundle;

public class AddDictTask implements IBcTask
{
	public static Intent createIntent (String name, String taskSuccessText)
	{
		Intent intent = new Intent();
		intent.putExtra(BundleFields.DICT_NAME, name);
		intent.putExtra(BundleFields.TASK_SUCCESS_TEXT, taskSuccessText);
		return intent;
	}

	private IBcToaster mBcToaster;
	private String mName;
	private String mTaskSuccessText;
	private IWStorage mDb;

	public void init(BcTaskEnv env)
	{
		mBcToaster = env.bcToaster;
		mName = env.extras.getString(BundleFields.DICT_NAME);
		mTaskSuccessText = env.extras.getString(BundleFields.TASK_SUCCESS_TEXT);
		mDb = DbCreator.createWritable(env.context);
	}

	public Bundle execute()
	{	
		long id = mDb.addDict(mName);
		if(id != -1 && mTaskSuccessText != null)
				mBcToaster.showToast(mTaskSuccessText);		
		mDb.destroy();	
		
		Bundle result = new Bundle();
		result.putLong(BundleFields.DICT_ID, id);
		return result;
	}

}