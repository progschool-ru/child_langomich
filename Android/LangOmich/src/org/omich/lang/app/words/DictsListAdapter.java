package org.omich.lang.app.words;

import java.util.ArrayList;
import java.util.List;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.Dict;
import org.omich.lang.app.db.IRStorage;
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

public class DictsListAdapter extends ListAdapter<Dict>
{
	private IBcConnector mConn;
	private String mLoadWordsTaskId;
	
	public DictsListAdapter (Context context, IBcConnector conn)
	{
		super(context);
		mConn = conn;
	}
	
	//==== ListAdapter ========================================================
	@Override
	protected void recycleItem (int position, Dict item) {}
	@Override
	protected int getItemViewResId () {return R.layout.app_item_wordslist;}

	@Override
	protected void fillViewByData (View view, int position, Dict item)
	{
		TextView tv = (TextView)view.findViewById(R.id.item_wordslist_text);
		tv.setText(item.name);
	}

	@Override
	public void reloadItems ()
	{
		if(mLoadWordsTaskId != null)
			return;

		IBcConnector conn = mConn;
		conn.startTask(BcService.class, LoadDictsTask.class, 
				LoadDictsTask.createIntent(), new IListener<Intent>()
				{
					public void handle(Intent value)
					{
						BcEventHelper.parseEvent(value, null, null, new IListener<Bundle>()
						{
							public void handle (Bundle b)
							{
								mLoadWordsTaskId = null;
			
								List<Dict> dicts = b.<Dict>getParcelableArrayList(BundleFields.DICTS_LIST);							
								setItems(dicts);
							}
						}, null, null);
					}
				});
	}
	
	//=========================================================================
	public static class LoadDictsTask implements IBcTask
	{
		public static Intent createIntent () {return new Intent();}

		private IRStorage mDb;

		public void init(Bundle extras, Context context, IBcToaster bcToaster)
		{
			mDb = DbCreator.createReadable(context);
		}

		public Bundle execute(IListenerInt ph, ICancelledInfo ci)
		{
			ArrayList<Dict> dicts = new ArrayList<Dict>(mDb.getDicts());
			Bundle result = new Bundle();
			result.putParcelableArrayList(BundleFields.DICTS_LIST, dicts);
			mDb.recycle();
			return result;
		}
	}
}