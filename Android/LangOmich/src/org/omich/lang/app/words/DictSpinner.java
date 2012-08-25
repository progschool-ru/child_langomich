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
	
	public static final int ADD_DICT = 1;
	public static final int SELECT_DICT = 2;
	
	public static final long NULL_DICT = -1;
	
	private boolean withNewDict;
	private boolean changeDictInPreferences;
	private long currentDictId;
	
	public DictSpinner(Spinner spinner, Context context, boolean withNewDict, boolean changeDictInPreferences, IListenerInt li)
	{		
		this.spinner = spinner;
		mBcConnector = new BcConnector(context);		
		sp = PreferenceManager.getDefaultSharedPreferences(context);		
		this.withNewDict = withNewDict;
		this.changeDictInPreferences = changeDictInPreferences;
		this.li = li;
		currentDictId = sp.getLong(PreferenceFields.DICT_ID, -1);
		
		dictsAdapterInitialization(context);		
		spinnerInitialization();
	}
	private void dictsAdapterInitialization(Context context)
	{
		mDictsAdapter = new DictsListAdapter(context, mBcConnector, withNewDict, new IListenerVoid()
		{
			public void handle ()
			{ 
				spinner.setSelection(getPositionFromListByIdInDB(currentDictId)); 
			}			
		});		
	}
	private void spinnerInitialization()
	{
		spinner.setAdapter(mDictsAdapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() 
        {
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{
        		itemIsSelected(position);
        	}
        	public void onNothingSelected(AdapterView<?> arg0) {}
        });
        mDictsAdapter.reloadItems();
	}	
	private int getPositionFromListByIdInDB(long dictId)
	{
		if(dictId == -1)
			return 0;
		int i = 0;
		int size = mDictsAdapter.getCount();
		for(i = 0; i < size; i++)			
			if(((Dict)mDictsAdapter.getItem(i)).dictId == dictId)
				break;
		return i;
	}
	public long getIdFromDBForSelectedItem()
	{
		return ((Dict)spinner.getSelectedItem()).dictId;
	}	
	private void itemIsSelected(int position)
	{
		boolean isLastItem = (position + 1 == mDictsAdapter.getCount());
		
		if(withNewDict && isLastItem)
		{
			li.handle(ADD_DICT);
		}
		else
		{	
			currentDictId = getIdFromDBForSelectedItem();
			if(changeDictInPreferences)
			{
				Editor ed = sp.edit();
				ed.putLong(PreferenceFields.DICT_ID, currentDictId);
				ed.commit();	
			}
			li.handle(SELECT_DICT);			
		}
	}
	public void reload(long dictId)
	{
		if(dictId != NULL_DICT)
			currentDictId = dictId;
		mDictsAdapter.reloadItems();
	}		
	public void destroy()
	{
		mDictsAdapter.destroy();
		mDictsAdapter = null;
	}
}
