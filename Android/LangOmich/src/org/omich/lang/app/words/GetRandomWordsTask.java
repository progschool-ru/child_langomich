package org.omich.lang.app.words;

import java.util.ArrayList;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IRStorage;
import org.omich.lang.app.db.Word;
import org.omich.tool.bcops.IBcTask;
import android.content.Intent;
import android.os.Bundle;

public class GetRandomWordsTask implements IBcTask
{
	public static Intent createIntent (long dictId, int n, int weight[]) 
	{
		Intent intent = new Intent();
		intent.putExtra(BundleFields.WORD_DICT_ID, dictId);
		intent.putExtra(BundleFields.WORDS_NUMBER, n);
		intent.putExtra(BundleFields.WORDS_WEIGHT, weight);
		return intent;
	}

	private IRStorage mDb;
	private Long mDictId;
	private int[] mWeight;
	private int mWordsNumber;

	@Override
	public void init(BcTaskEnv env)
	{
		mDictId = env.extras.getLong(BundleFields.WORD_DICT_ID);
		mWordsNumber = env.extras.getInt(BundleFields.WORDS_NUMBER);
		mWeight = env.extras.getIntArray(BundleFields.WORDS_WEIGHT);
		mDb = DbCreator.createReadable(env.context);
	}

	@Override
	public Bundle execute()
	{		
		ArrayList<Word> words = new ArrayList<Word>(mDb.getRandomWords(mDictId, mWordsNumber, mWeight));
		Bundle result = new Bundle();
		result.putParcelableArrayList(BundleFields.WORDS_LIST, words);
		mDb.destroy();
		return result;
	}

}