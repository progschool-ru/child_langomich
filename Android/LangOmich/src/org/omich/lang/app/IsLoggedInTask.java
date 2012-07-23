package org.omich.lang.app;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.httpClient.SmdClient;
import org.omich.tool.bcops.IBcTask;

import android.content.Intent;
import android.os.Bundle;
public class IsLoggedInTask implements IBcTask
	{
		public static Intent createIntent (String cookie)
		{
			Intent intent = new Intent();
			intent.putExtra(BundleFields.COOKIE, cookie);
			return intent;
		}
		private String mCookie;

		public void init(BcTaskEnv env)
		{
			mCookie = env.extras.getString(BundleFields.COOKIE);
		}

		public Bundle execute()
		{
			boolean isLoggedIn = false;
			Bundle result = new Bundle();
			SmdClient hr = new SmdClient();	
			try
			{
				hr.setCookie(mCookie);
				isLoggedIn = hr.isLoggedIn();	
			}			
			catch(Exception e){}
			result.putBoolean(BundleFields.IS_LOGGED_IN, isLoggedIn);
			return result;
		}
	}