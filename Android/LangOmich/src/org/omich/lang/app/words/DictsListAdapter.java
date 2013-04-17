package org.omich.lang.app.words;

import java.util.ArrayList;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.Dict;
import org.omich.lang.app.db.IRStorage;
import org.omich.lang.apptool.lists.TaskListAdapter;
import org.omich.tool.bcops.IBcConnector;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.events.Listeners.IListenerVoid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DictsListAdapter extends TaskListAdapter<Dict>
{	
	boolean withNewDict = true;
	public DictsListAdapter (Context context, IBcConnector conn, boolean withNewDict)
	{		
		super(context, conn);
		this.withNewDict = withNewDict;
	}
	public DictsListAdapter (Context context, IBcConnector conn, boolean withNewDict, IListenerVoid lv)
	{
		super(context, conn, lv);
		this.withNewDict = withNewDict;
	}	
	//==== TaskListAdapter ===================================================
	@Override
	protected Class<? extends IBcTask> getLoadItemsTaskClass (){return LoadDictsTask.class;}
	@Override
	public Intent createLoadItemsIntent (){return LoadDictsTask.createIntent(withNewDict);}
	@Override
	protected String getListBundleField (){return BundleFields.DICTS_LIST;}

	//==== ListAdapter ========================================================
	@Override
	protected void destroyItem (int position, Dict item) {}
	@Override
	protected int getItemViewResId (int position) {return R.layout.app_item_dictslist;}

	@Override
	protected void fillViewByData (View view, int position, Dict item)
	{
		TextView tv = (TextView)view.findViewById(R.id.item_dictslist_text);
		tv.setTextColor(0xff000000);
		tv.setText(item.name);
	}
	
	//=========================================================================
	public static class LoadDictsTask implements IBcTask
	{
		public static Intent createIntent (boolean withNewDict) 
		{
			Intent intent = new Intent();
			intent.putExtra(BundleFields.DICTS_WITH_NEW_DICT, withNewDict);
			return intent;
		}

		private IRStorage mDb;
		private String mNewDictText;
		private boolean mWithNewDict;

		@Override
		public void init(BcTaskEnv env)
		{
			mWithNewDict = env.extras.getBoolean(BundleFields.DICTS_WITH_NEW_DICT);
			mDb = DbCreator.createReadable(env.context);
			mNewDictText = env.context.getResources().getString(R.string.adddict_title);
		}

		@Override
		public Bundle execute()
		{
			ArrayList<Dict> dicts = new ArrayList<Dict>(mDb.getDicts());
			if(mWithNewDict)
				dicts.add(new Dict(-1, mNewDictText));
			Bundle result = new Bundle();
			result.putParcelableArrayList(BundleFields.DICTS_LIST, dicts);
			mDb.destroy();
			return result;
		}
	}
}