package org.omich.lang.app.words;

import java.util.ArrayList;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IRStorage;
import org.omich.lang.app.db.Word;
import org.omich.lang.apptool.lists.TaskListAdapter;
import org.omich.tool.bcops.IBcConnector;
import org.omich.tool.bcops.IBcTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WordsListAdapter extends TaskListAdapter<Word>
{
	public WordsListAdapter (Context context, IBcConnector conn)
	{
		super(context, conn);
	}

	//==== TaskListAdapter ===================================================
	@Override
	protected Class<? extends IBcTask> getLoadItemsTaskClass (){return LoadWordsTask.class;}
	@Override
	public Intent createLoadItemsIntent (){return LoadWordsTask.createIntent();}
	@Override
	protected String getListBundleField (){return BundleFields.WORDS_LIST;}
	
	//==== ListAdapter ========================================================
	@Override
	protected void destroyItem (int position, Word item) {}
	@Override
	protected int getItemViewResId () {return R.layout.app_item_wordslist;}

	@Override
	protected void fillViewByData (View view, int position, Word item)
	{
		TextView tvn = (TextView)view.findViewById(R.id.item_wordslist_text_nativ);
		tvn.setText(item.nativ);
		TextView tvf = (TextView)view.findViewById(R.id.item_wordslist_text_foreign);
		tvf.setText(item.foreign);
		System.out.println(item.rating);
		TextView tvr = (TextView)view.findViewById(R.id.item_wordslist_text_rating);
		tvr.setText(Integer.toString(item.rating));
	}
	//=========================================================================
	public static class LoadWordsTask implements IBcTask
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
			Bundle result = new Bundle();
			result.putParcelableArrayList(BundleFields.WORDS_LIST, words);
			mDb.destroy();
			return result;
		}
	}
}
