package org.omich.lang.apptool.activity;

import java.util.ArrayList;

import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.Dict;
import org.omich.lang.app.db.Word;
import org.omich.lang.app.httpClient.SmdClient;
import org.omich.tool.bcops.IBcTask;


import android.content.Intent;
import android.os.Bundle;

public class TimingSendWordsTask implements IBcTask
{
	public static Intent createIntent (ArrayList<Word> words, ArrayList<Dict> dicts, long serverTime, String cookie)
	{
		Intent intent = new Intent();
		intent.putExtra(BundleFields.WORDS_LIST, words);
		intent.putExtra(BundleFields.DICTS_LIST, dicts);
		intent.putExtra(BundleFields.SERVER_TIME, serverTime);
		intent.putExtra(BundleFields.COOKIE, cookie);
		return intent;
	}
	private ArrayList<Word> mWords;
	private ArrayList<Dict> mDicts;
	private long mServerTime;
	private String mCookie;
	
	public void init(BcTaskEnv env)
	{
		mWords = env.extras.getParcelableArrayList(BundleFields.WORDS_LIST);
		mDicts = env.extras.getParcelableArrayList(BundleFields.DICTS_LIST);
		mServerTime = env.extras.getLong(BundleFields.SERVER_TIME);
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
		result.putParcelableArrayList(BundleFields.WORDS_LIST, mWords);
		result.putParcelableArrayList(BundleFields.DICTS_LIST, mDicts);
		result.putLong(BundleFields.SERVER_TIME, mServerTime);
		return result;
	}

}