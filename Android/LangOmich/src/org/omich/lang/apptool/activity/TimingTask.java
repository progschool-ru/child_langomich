package org.omich.lang.apptool.activity;

import java.util.ArrayList;
import java.util.List;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.Dict;
import org.omich.lang.app.db.IRStorage;
import org.omich.lang.app.db.Word;
import org.omich.lang.app.httpClient.SmdClient;
import org.omich.tool.bcops.IBcTask;


import android.content.Intent;
import android.os.Bundle;

public class TimingTask implements IBcTask
{
	public static Intent createIntent (long mobileTime,long serverTime, String cookie)
	{
		Intent intent = new Intent();
		intent.putExtra(BundleFields.MOBILE_TIME, mobileTime);
		return intent;
	}
	private IRStorage mDb;
	private long mMobileTime;

	public void init(BcTaskEnv env)
	{
		mMobileTime = env.extras.getLong(BundleFields.MOBILE_TIME);
		mDb = DbCreator.createReadable(env.context);
	}

	public Bundle execute()
	{
		List<Word> words = new ArrayList<Word>(mDb.getWords(mMobileTime));	
		List<Dict> dicts = new ArrayList<Dict>(mDb.getDicts(mMobileTime));
		
/*		boolean isLoggedIn = false;

		Bundle result = new Bundle();
		SmdClient hr = new SmdClient();	
		try
		{
			hr.setCookie(mCookie);

			isLoggedIn = hr.isLoggedIn();	
		}			
		catch(Exception e){}	
		mDb.addDicts(dicts);
		mDb.addWords(words);
		*/
		mDb.destroy();
		return null;
	}

}