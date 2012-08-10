package org.omich.lang.apptool.activity;

import java.util.ArrayList;
import java.util.List;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.Dict;
import org.omich.lang.app.db.IRStorage;
import org.omich.lang.app.db.IWStorage;
import org.omich.lang.app.db.Word;
import org.omich.lang.app.httpClient.SmdClient;
import org.omich.tool.bcops.IBcTask;
import org.omich.tool.bcops.IBcToaster;

import android.content.Intent;
import android.os.Bundle;

public class TimingTask implements IBcTask
{
	public static Intent createIntent (long mobileTime, long serverTime, String cookie, String taskSuccessText)
	{
		Intent intent = new Intent();
		intent.putExtra(BundleFields.MOBILE_TIME, mobileTime);
		intent.putExtra(BundleFields.SERVER_TIME, serverTime);
		intent.putExtra(BundleFields.COOKIE, cookie);
		intent.putExtra(BundleFields.TASK_SUCCESS_TEXT, taskSuccessText);
		return intent;
	}
	private IRStorage mDbR;
	private IWStorage mDbW;
	private long mMobileTime;
	private long mServerTime;
	private String mCookie;
	private IBcToaster mBcToaster;
	private String mTaskSuccessText;

	public void init(BcTaskEnv env)
	{
		mBcToaster = env.bcToaster;
		mMobileTime = env.extras.getLong(BundleFields.MOBILE_TIME);
		mServerTime = env.extras.getLong(BundleFields.SERVER_TIME);
		mCookie = env.extras.getString(BundleFields.COOKIE);
		mTaskSuccessText = env.extras.getString(BundleFields.TASK_SUCCESS_TEXT);
		mDbR = DbCreator.createReadable(env.context);
		mDbW = DbCreator.createWritable(env.context);
	}

	public Bundle execute()
	{
		boolean success = false;
		List<Word> words = new ArrayList<Word>(mDbR.getWordsByTime(mMobileTime));	
		List<Dict> dicts = new ArrayList<Dict>(mDbR.getDicts(mMobileTime));	
		mDbR.destroy();
		
		SmdClient hr = new SmdClient();	
		try
		{
			hr.setCookie(mCookie);	
			mServerTime = hr.timing(words, dicts, mServerTime, mDbW);
			success = true;
		}			
		catch(Exception e){}	
		
		Bundle result = new Bundle();
		result.putLong(BundleFields.SERVER_TIME, mServerTime);

		if(success && mTaskSuccessText != null)
		{	
			mBcToaster.showToast(mTaskSuccessText);
		}
		
		mDbW.destroy();
		return result;
	}

}