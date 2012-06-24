package org.omich.lang.apptool.activity;

import org.omich.tool.bcops.BcConnector;
import org.omich.tool.bcops.IBcConnector;

import android.os.Bundle;

public class BcActivity extends AppActivity
{
	private BcConnector mBcConnector;

	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	
		mBcConnector = new BcConnector(this);
	}

	@Override
	protected void onDestroy ()
	{
		mBcConnector.destroy();
		mBcConnector = null;
		super.onDestroy();
	}
	
	//==== protected interface ===============================================
	protected IBcConnector getBcConnector (){return mBcConnector;}
}
