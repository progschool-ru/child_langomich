package org.omich.lang.app;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.httpClient.SmdClient;
import org.omich.tool.bcops.IBcTask;

import android.content.Intent;
import android.os.Bundle;
public class TheCorrectAccountTask implements IBcTask
	{
		public static Intent createIntent (String login, String password)
		{
			Intent intent = new Intent();
			intent.putExtra(BundleFields.ACCOUNT_LOGIN, login);
			intent.putExtra(BundleFields.ACCOUNT_PASSWORD, password);
			return intent;
		}

		private String mLogin;
		private String mPassword;

		public void init(BcTaskEnv env)
		{
			mLogin = env.extras.getString(BundleFields.ACCOUNT_LOGIN);
			mPassword = env.extras.getString(BundleFields.ACCOUNT_PASSWORD);
		}

		public Bundle execute()
		{
			boolean correctAccount = false;
			Bundle result = new Bundle();
			SmdClient hr = new SmdClient();		
			try
			{
				correctAccount = hr.auth(mLogin, mPassword);
				result.putString(BundleFields.COOKIE, hr.getCookie());	
			}			
			catch(Exception e){}
			result.putBoolean(BundleFields.CORRECT_ACCOUNT, correctAccount);
			return result;
		}
	}