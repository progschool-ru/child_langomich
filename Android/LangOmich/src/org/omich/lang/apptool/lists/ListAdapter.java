package org.omich.lang.apptool.lists;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

abstract public class ListAdapter<Item> extends BaseAdapter
{
	private Context mContext;
	private List<Item> mItems = new ArrayList<Item>();
	private boolean mIsDestroyed;
	private int selectedPosition = -1;
	
	protected ListAdapter (Context context)
	{
		mContext = context;
	}
	
	

	//==== public interface ===================================================
	abstract public void reloadItems ();
	
	public void destroy ()
	{
		cleanItems();
		mItems = null;
		mContext = null;
		mIsDestroyed = true;
	}
	public void setSelectedPosition(int selectedPosition)
	{
		this.selectedPosition = selectedPosition;
	}
	public int getSelectedPosition()
	{
		return selectedPosition;
	}	
	//==== protected interface ================================================
	abstract protected void destroyItem (int position, Item item);
	abstract protected int getItemViewResId (int position);
	abstract protected void fillViewByData (View view, int position, Item item);

	protected void setItems (List<Item> items)
	{
		if(mIsDestroyed)
			return;

		cleanItems ();
		mItems = items;
		notifyDataSetChanged();
	}

	//==== BaseAdapter ========================================================
	@Override
	public int getCount () {return mItems.size();}
	@Override
	public Object getItem (int arg0) {return mItems.get(arg0);}
	@Override
	public long getItemId (int position) {return position;}

	@Override
	public View getView (int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		
		if(v == null)
		{
			LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(getItemViewResId(position), null);
		}

		fillViewByData(v, position, mItems.get(position));
		
		return v;
	}
	
	//=========================================================================
	private void cleanItems ()
	{
		int size = mItems.size();
		for(int i = 0; i < size; ++i)
		{
			destroyItem(i, mItems.get(i));
		}
		mItems.clear();
	}
}
