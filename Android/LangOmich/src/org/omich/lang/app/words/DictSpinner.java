package org.omich.lang.app.words;

import org.omich.lang.app.PreferenceFields;
import org.omich.lang.app.db.Dict;
import org.omich.tool.bcops.BcConnector;
import org.omich.tool.events.Listeners.IListenerInt;
import org.omich.tool.events.Listeners.IListenerVoid;

import android.content.Context;
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
	private DictsListAdapter mDictsAdapter;
	private SharedPreferences sp;
	private BcConnector mBcConnector;
	private IListenerInt li;
	private int size = 1;
	
	public final int ADD_DICT = 1;
	public final int SELECT_DICT = 2;
	
	private boolean withNewDict = false;
	private boolean changeDictInProgram = true;
	
	public DictSpinner(Spinner spinner, Context context, boolean withNewDict, boolean changeDictInProgram, IListenerInt li)
	{
		
		this.spinner = spinner;
		this.li = li;
		this.withNewDict = withNewDict;
		this.changeDictInProgram = changeDictInProgram;
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		mBcConnector = new BcConnector(context);
		
		mDictsAdapter = new DictsListAdapter(context, mBcConnector, withNewDict, new IListenerVoid()
		{
			public void handle ()
			{ 
				setSelection();
			}			
		});
		mDictsAdapter.reloadItems();		
		
		spinner.setAdapter(mDictsAdapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{
        		size = mDictsAdapter.getCount();
        		if(position + 1 == size) 
        			onAddDict();     			
        		else
        			onSelectDict();
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
	public long getSelectedItemTableId()
	{
		return ((Dict)spinner.getSelectedItem()).dictId;
	}			
	private void onAddDict()
	{
		if(withNewDict)
		{
			li.handle(ADD_DICT);
			setSelection();
		}
		else
			onSelectDict();
	}
	private void onSelectDict()
	{
		if(changeDictInProgram)
		{
			long dictId = getSelectedItemTableId();
			Editor ed = sp.edit();
			ed.putLong(PreferenceFields.DICT_ID, dictId);
			ed.commit();	
		}
		li.handle(SELECT_DICT);
	}	
	public DictsListAdapter getDictsAdapter()
	{
		return mDictsAdapter;
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
