package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.app.db.Dict;
import org.omich.lang.apptool.activity.BcActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DictsListForChooseActivity extends BcActivity
{
	private DictsListAdapter mDictsAdapter;
	private Dict dict;
	
	//==== live cycle =========================================================
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_dialog_dictslist);	
		ListView lv = (ListView)findViewById(R.id.dictslist_list);
		mDictsAdapter = new DictsListAdapter(this, getBcConnector(), false);
		mDictsAdapter.reloadItems();	
		lv.setAdapter(mDictsAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				dict = (Dict)mDictsAdapter.getItem(position);
			    Intent intent = new Intent();
			    intent.putExtra("dictId", dict.dictId);
			    setResult(RESULT_OK, intent);
			    finish();				
			}
		});		
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
	@Override
	protected void onResume()
	{
		mDictsAdapter.reloadItems();
		super.onResume();
	}
	@Override
	protected void onDestroy ()
	{
		mDictsAdapter.destroy();
		mDictsAdapter = null;
		
		super.onDestroy();
	}
	public void onCancelButton (View v)
	{
		finish();
	}		
}
