package org.omich.lang.apptool.activity;

import java.util.ArrayList;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.Dict;
import org.omich.lang.app.db.IRStorage;
import org.omich.lang.app.db.Word;
import org.omich.tool.bcops.IBcTask;


import android.content.Intent;
import android.os.Bundle;

public class TimingGetWordsTask implements IBcTask
{
	public static Intent createIntent (long mobileTime)
	{
		Intent intent = new Intent();
		intent.putExtra(BundleFields.MOBILE_TIME, mobileTime);
		return intent;
	}
	private IRStorage mDb;
	private long mMobileTime;

	public void init(BcTaskEnv env)
	{
		mMobileTime = env.extras.getLong(BundleFields.MOBILE_TIME);
		mDb = DbCreator.createReadable(env.context);
	}

	public Bundle execute()
	{
		ArrayList<Word> words = new ArrayList<Word>(mDb.getWords(mMobileTime));	
		ArrayList<Dict> dicts = new ArrayList<Dict>(mDb.getDicts(mMobileTime));
		Bundle result = new Bundle();
		result.putParcelableArrayList(BundleFields.WORDS_LIST, words);
		result.putParcelableArrayList(BundleFields.DICTS_LIST, dicts);
		mDb.destroy();
		return result;
	}

}