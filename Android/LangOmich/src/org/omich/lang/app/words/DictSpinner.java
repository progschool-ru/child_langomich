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
        		int size = mDictsAdapter.getCount();
        		if(position + 1 == size) 
        		{
        			onAddDict();
        			setSelection();
        		}
        		else
        		{
        			long dictId = getSelectedItem();
        			Editor ed = sp.edit();
        			ed.putInt(PreferenceFields.DICT_POSITION, position);
        			ed.putLong(PreferenceFields.DICT_ID, dictId);
        			ed.commit();
        		}
        	}
        	public void onNothingSelected(AdapterView<?> arg0) {}
        });
	}
	private void setSelection()
	{
		 spinner.setSelection(sp.getInt(PreferenceFields.DICT_POSITION, 0)); 
	}
	private long getSelectedItem()
	{
		return ((Dict)spinner.getSelectedItem()).dictId;
	}	
	private void onAddDict()
	{
		context.startActivity(new Intent(context, AddDictActivity.class));
	}	
	public void destroy()
	{
		mDictsAdapter.destroy();
		mDictsAdapter = null;
	}
}
