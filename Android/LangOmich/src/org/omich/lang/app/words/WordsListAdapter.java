package org.omich.lang.app.words;

import java.util.ArrayList;
import java.util.List;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IRStorage;
import org.omich.lang.app.db.Word;
import org.omich.lang.apptool.events.Listeners.IListener;
import org.omich.lang.apptool.events.Listeners.IListenerInt;
import org.omich.lang.apptool.lists.ListAdapter;
import org.omich.tool.bcops.BcEventHelper;
import org.omich.tool.bcops.BcService;
import org.omich.tool.bcops.IBcConnector;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.bcops.IBcToaster;
import org.omich.tool.bcops.ICancelledInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WordsListAdapter extends ListAdapter<Word>
{
	private IBcConnector mConn;
	private String mLoadWordsTaskId;
	
	public WordsListAdapter (Context context, IBcConnector conn)
	{
		super(context);
		mConn = conn;
	}
	
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

	@Override
	public void reloadItems ()
	{
		if(mLoadWordsTaskId != null)
			return;

		IBcConnector conn = mConn;
		conn.startTask(BcService.class, LoadWordsTask.class, 
				LoadWordsTask.createIntent(), new IListener<Intent>()
				{
					public void handle(Intent value)
					{
						BcEventHelper.parseEvent(value, null, null, new IListener<Bundle>()
						{
							public void handle (Bundle b)
							{
								mLoadWordsTaskId = null;
			
								List<Word> words = b.<Word>getParcelableArrayList(BundleFields.WORDS_LIST);							
								setItems(words);
							}
						}, null, null);
					}
				});
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
