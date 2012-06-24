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
import org.omich.tool.bcops.BcEventHelper;
import org.omich.tool.bcops.BcService;
import org.omich.tool.bcops.IBcConnector;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.bcops.IBcToaster;
import org.omich.tool.bcops.ICancelledInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DictsListAdapter extends BaseAdapter
{
	private Context mContext;

	private List<Dict> mDicts = new ArrayList<Dict>();
	private String mLoadWordsTaskId;
	
	public DictsListAdapter (Context context)
	{
		mContext = context;
	}
	
	public void reloadDicts (IBcConnector conn)
	{
		if(mLoadWordsTaskId != null)
			return;

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
			
								mDicts = b.<Dict>getParcelableArrayList(BundleFields.DICTS_LIST);							
								notifyDataSetChanged();
							}
						}, null, null);
					}
				});
	}

	//==== BaseAdapter ========================================================
	public int getCount () {return mDicts.size();}
	public Object getItem (int arg0)	{return mDicts.get(arg0);}
	public long getItemId (int position) {return position;}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		
		if(v == null)
		{
			LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.app_item_wordslist, null);
		}
		
		TextView tv = (TextView)v.findViewById(R.id.item_wordslist_text);
		tv.setText(mDicts.get(position).name);

		return v;
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