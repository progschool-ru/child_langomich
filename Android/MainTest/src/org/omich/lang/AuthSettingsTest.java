package org.omich.lang;

import junit.framework.Assert;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;

public class AuthSettingsTest extends ActivityInstrumentationTestCase2<Main> {
	
	private Activity mActivity;
	private AuthSettigs mSettigs;
	private static final String MY_PREFERS = "MyPrefers";
	
	public AuthSettingsTest(){
		super("org.omich.lang",Main.class);
	}
	
	@Override
	public void setUp(){
		mActivity = getActivity();
		mSettigs = new AuthSettigs(MY_PREFERS, mActivity);
		
	}
	
	public void test1(){
		mSettigs.clear();
		assertTrue(mSettigs.saveAuthData("login", "password"));
		assertEquals(mSettigs.getLogin(), "login");
		assertEquals(mSettigs.getPassword(), "password");
	}
	
	public void test2(){
		mSettigs.clear();
		mSettigs.saveAuthData("", "password");
		assertEquals(mSettigs.getLogin(), "");
		assertEquals(mSettigs.getPassword(), "");
	}
}
