package org.omich.lang.app.words;

import java.util.ArrayList;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IRStorage;
import org.omich.lang.app.db.Word;
import org.omich.tool.bcops.IBcTask;


import android.content.Intent;
import android.os.Bundle;

public class GetWordTask implements IBcTask
{
	public static Intent createIntent () {return new Intent();}

	private IRStorage mDb;

	public void init(BcTaskEnv env)
	{
		mDb = DbCreator.createReadable(env.context);
	}

	public Bundle execute()
	{
		ArrayList<Word> words = new ArrayList<Word>(mDb.getWords());
		Word word = words.get(1);
		Bundle result = new Bundle();
		result.putParcelable(BundleFields.WORD, word);
		mDb.destroy();
		return result;
	}

}