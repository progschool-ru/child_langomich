package org.omich.lang.app;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.httpClient.SmdClient;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.bcops.IBcToaster;

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
		private IBcToaster mBcToaster;
		private String mTaskSuccessText;
		private String mCookie;

		public void init(BcTaskEnv env)
		{
			mBcToaster = env.bcToaster;
			mCookie = env.extras.getString(BundleFields.COOKIE);
			mTaskSuccessText = env.extras.getString(BundleFields.TASK_SUCCESS_TEXT);
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
			result.putBoolean(BundleFields.CORRECT_ACCOUNT, isLoggedIn);
			return result;
		}
	}