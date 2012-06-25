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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DictsListAdapter extends TaskListAdapter<Dict>
{	
	public DictsListAdapter (Context context, IBcConnector conn)
	{
		super(context, conn);
	}
	
	//==== TaskListAdapter ===================================================
	@Override
	protected Class<? extends IBcTask> getLoadItemsTaskClass (){return LoadDictsTask.class;}
	@Override
	public Intent createLoadItemsIntent (){return LoadDictsTask.createIntent();}
	@Override
	protected String getListBundleField (){return BundleFields.DICTS_LIST;}

	//==== ListAdapter ========================================================
	@Override
	protected void destroyItem (int position, Dict item) {}
	@Override
	protected int getItemViewResId () {return R.layout.app_item_wordslist;}

	@Override
	protected void fillViewByData (View view, int position, Dict item)
	{
		TextView tv = (TextView)view.findViewById(R.id.item_wordslist_text);
		tv.setTextColor(0xff000000);
		tv.setText(item.name);
	}
	
	//=========================================================================
	public static class LoadDictsTask implements IBcTask
	{
		public static Intent createIntent () {return new Intent();}

		private IRStorage mDb;

		public void init(BcTaskEnv env)
		{
			mDb = DbCreator.createReadable(env.context);
		}

		public Bundle execute()
		{
			ArrayList<Dict> dicts = new ArrayList<Dict>(mDb.getDicts());
			Bundle result = new Bundle();
			result.putParcelableArrayList(BundleFields.DICTS_LIST, dicts);
			mDb.destroy();
			return result;
		}
	}
}