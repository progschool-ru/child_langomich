package org.omich.lang.app;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.httpClient.SmdClient;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.bcops.IBcToaster;

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
		private IBcToaster mBcToaster;
		private String mTaskSuccessText;
		private String mLogin;
		private String mPassword;

		public void init(BcTaskEnv env)
		{
			mBcToaster = env.bcToaster;
			mLogin = env.extras.getString(BundleFields.ACCOUNT_LOGIN);
			mPassword = env.extras.getString(BundleFields.ACCOUNT_PASSWORD);
			mTaskSuccessText = env.extras.getString(BundleFields.TASK_SUCCESS_TEXT);
		}

		public Bundle execute()
		{
			boolean correctAccount = false;
			SmdClient hr = new SmdClient();		
			try{
				correctAccount = hr.auth(mLogin, mPassword);}
			catch(Exception e){}
			Bundle result = new Bundle();
			result.putBoolean(BundleFields.CORRECT_ACCOUNT, correctAccount);
			return result;
		}
	}