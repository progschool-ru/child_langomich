package org.omich.lang.apptool.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AppActivity extends Activity
{
	private ForResultStarter mForResultStarter;
	
	//==== live cycle =========================================================
		@Override
		protected void onCreate (Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);	
			mForResultStarter = new ForResultStarter(this);
		}

		@Override
		protected void onActivityResult (int reqCode, int resCode, Intent data)
		{
			if(mForResultStarter == null || !mForResultStarter.onActivityResult(reqCode, resCode, data))
			{
				super.onActivityResult(reqCode, resCode, data);
			}
		}
		
		@Override
		protected void onDestroy ()
		{
			mForResultStarter.destroy();
			mForResultStarter = null;
			super.onDestroy();
		}

		//==== protected interface ===============================================
		protected IForResultStarter getForResultStarter (){return mForResultStarter;}
}
