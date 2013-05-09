package org.omich.lang.apptool.activity;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.os.Bundle;

public class ABActivity extends BcActivity
{
	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	

	}
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) 
	{
//		  MenuInflater inflater = getMenuInflater();
//		  inflater.inflate(R.menu.menu, menu);		  
//		  itemTiming = menu.findItem(R.id.app_menu_item_timing);
//		  if(sp.getBoolean(PreferenceFields.IS_TIMING, false))
//			  itemTiming.setIcon(R.drawable.ic_sunc_enable);
//		  else
	//		  itemTiming.setIcon(R.drawable.ic_sunc_disable);
		  return true;
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
/*       switch (item.getItemId()) 
        {
	        case R.id.app_menu_item_timing:
	        	timing();
	        	if(!sp.getBoolean(PreferenceFields.IS_TIMING, false))
	        	{
	        		timing();
	        	}
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }*/
		return true;
    }	
}