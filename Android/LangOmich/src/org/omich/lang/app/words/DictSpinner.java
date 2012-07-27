package org.omich.lang.app.words;

import org.omich.lang.app.PreferenceFields;
import org.omich.lang.app.db.Dict;
import org.omich.tool.bcops.BcConnector;
import org.omich.tool.events.Listeners.IListenerBoolean;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class DictSpinner
{
	private Spinner spinner;
	private Context context;
	private DictsListAdapter mDictsAdapter;
	private SharedPreferences sp;
	private BcConnector mBcConnector;
	private int size = 1;
	public DictSpinner(Spinner spinner, Context context)
	{
		
		this.spinner = spinner;
		this.context = context;
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		mBcConnector = new BcConnector(context);
		
		mDictsAdapter = new DictsListAdapter(context, mBcConnector, new IListenerBoolean()
		{
			public void handle (boolean b)
			{ 
				if(b) setSelection();
			}			
		});
		mDictsAdapter.reloadItems();		
		
		spinner.setAdapter(mDictsAdapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{
        		size = mDictsAdapter.getCount();
        		if(position + 1 == size) 
        		{
        			onAddDict();
        			setSelection();
        		}
        		else
        		{
        			long dictId = getSelectedItemTableId();
        			Editor ed = sp.edit();
        			ed.putLong(PreferenceFields.DICT_ID, dictId);
        			ed.commit();       			
        		}
        	}
        	public void onNothingSelected(AdapterView<?> arg0) {}
        });
	}
	private void setSelection()
	{
		long dictId = sp.getLong(PreferenceFields.DICT_ID, -1);
		if(dictId != -1)
		{
			spinner.setSelection(getPositionByTableId(dictId)); 
		}
	}
	private int getPositionByTableId(long dictId)
	{
		int i = 0;
		int size = mDictsAdapter.getCount();
		for(i = 0; i < size; i++)			
			if(((Dict)mDictsAdapter.getItem(i)).dictId == dictId)
				break;
		return i;
	}
	private long getSelectedItemTableId()
	{
		return ((Dict)spinner.getSelectedItem()).dictId;
	}			
	private void onAddDict()
	{
		context.startActivity(new Intent(context, AddDictActivity.class));
	}
	public void reload()
	{
		mDictsAdapter.reloadItems();
	}		
	public void destroy()
	{
		mDictsAdapter.destroy();
		mDictsAdapter = null;
	}
}
