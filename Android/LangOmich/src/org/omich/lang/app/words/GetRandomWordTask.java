package org.omich.lang.app.words;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IRStorage;
import org.omich.lang.app.db.Word;
import org.omich.tool.bcops.IBcTask;


import android.content.Intent;
import android.os.Bundle;

public class GetRandomWordTask implements IBcTask
{
	public static Intent createIntent (long dictId) 
	{
		Intent intent = new Intent();
		intent.putExtra(BundleFields.WORD_DICT_ID, dictId);
		return intent;
	}

	private IRStorage mDb;
	private Long mDictId;

	public void init(BcTaskEnv env)
	{
		mDictId = env.extras.getLong(BundleFields.WORD_DICT_ID);
		mDb = DbCreator.createReadable(env.context);
	}

	public Bundle execute()
	{
		Word word = mDb.getRandomWord(mDictId);
		Bundle result = new Bundle();
		result.putParcelable(BundleFields.WORD, word);
		mDb.destroy();
		return result;
	}

}