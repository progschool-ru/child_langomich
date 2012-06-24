package org.omich.lang.app.words;

import java.util.ArrayList;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IRStorage;
import org.omich.lang.app.db.Word;
import org.omich.lang.apptool.events.Listeners.IListenerInt;
import org.omich.lang.apptool.lists.TaskListAdapter;
import org.omich.tool.bcops.IBcConnector;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.bcops.IBcToaster;
import org.omich.tool.bcops.ICancelledInfo;

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
	protected void recycleItem (int position, Word item) {}
	@Override
	protected int getItemViewResId () {return R.layout.app_item_wordslist;}

	@Override
	protected void fillViewByData (View view, int position, Word item)
	{
		TextView tv = (TextView)view.findViewById(R.id.item_wordslist_text);
		tv.setText(item.nativ);
	}

	//=========================================================================
	public static class LoadWordsTask implements IBcTask
	{
		public static Intent createIntent () {return new Intent();}

		private IRStorage mDb;

		public void init(Bundle extras, Context context, IBcToaster bcToaster)
		{
			mDb = DbCreator.createReadable(context);
		}

		public Bundle execute(IListenerInt ph, ICancelledInfo ci)
		{
			ArrayList<Word> words = new ArrayList<Word>(mDb.getWords());
			Bundle result = new Bundle();
			result.putParcelableArrayList(BundleFields.WORDS_LIST, words);
			mDb.recycle();
			return result;
		}
	}
}
