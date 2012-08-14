package org.omich.tool.activity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 *  Активити со встроенноым ForResultStarter.
 * 
 *  Хороший родитель для Активити без иерархии.
 *  
 *  Пример того, как надо правильно встраивать ForResultStarter в какое-либо активити.
 *
 */
public class ForResultActivity extends Activity
{
	private @Nonnull ForResultStarter mForResultStarter = new ForResultStarter(this);

	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	
	}

	@Override
	protected void onActivityResult (int reqCode, int resCode, @Nullable Intent data)
	{
		if(!mForResultStarter.onActivityResult(reqCode, resCode, data))
		{
			//Если mForResultStarter не нашёл обработчика, то передаём обработку вверх по иерархии
			super.onActivityResult(reqCode, resCode, data);
		}
	}
	
	@Override
	protected void onDestroy ()
	{
		mForResultStarter.destroy();
		super.onDestroy();
	}

	//==== protected interface ===============================================
	protected @Nonnull IForResultStarter getForResultStarter (){return mForResultStarter;}
}
