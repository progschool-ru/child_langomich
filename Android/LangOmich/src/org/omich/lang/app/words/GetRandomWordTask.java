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
	public static Intent createIntent () {return new Intent();}

	private IRStorage mDb;

	public void init(BcTaskEnv env)
	{
		mDb = DbCreator.createReadable(env.context);
	}

	public Bundle execute()
	{
		Word word = mDb.getRandomWord();
		Bundle result = new Bundle();
		result.putParcelable(BundleFields.WORD, word);
		mDb.destroy();
		return result;
	}

}