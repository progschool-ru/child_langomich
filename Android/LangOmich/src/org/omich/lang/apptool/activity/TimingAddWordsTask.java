package org.omich.lang.apptool.activity;

import java.util.ArrayList;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.Dict;
import org.omich.lang.app.db.IWStorage;
import org.omich.lang.app.db.Word;
import org.omich.tool.bcops.IBcTask;
import android.content.Intent;
import android.os.Bundle;

public class TimingAddWordsTask implements IBcTask
{
	public static Intent createIntent (ArrayList<Word> words, ArrayList<Dict> dicts)
	{
		Intent intent = new Intent();
		intent.putExtra(BundleFields.WORDS_LIST, words);
		intent.putExtra(BundleFields.DICTS_LIST, dicts);
		return intent;
	}

	private ArrayList<Word> mWords;
	private ArrayList<Dict> mDicts;
	private IWStorage mDb;

	public void init(BcTaskEnv env)
	{
		mWords = env.extras.getParcelableArrayList(BundleFields.WORDS_LIST);
		mDicts = env.extras.getParcelableArrayList(BundleFields.DICTS_LIST);
		mDb = DbCreator.createWritable(env.context);
	}

	public Bundle execute()
	{	
		mDb.addDicts(mDicts);
		mDb.addWords(mWords);
		mDb.destroy();
		return null;
	}

}