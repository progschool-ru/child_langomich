package org.omich.lang.test;

import org.omich.lang.AuthSettigs;
import org.omich.lang.MainActivity;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

public class AuthSettingsTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private Activity mActivity;
	private AuthSettigs mSettigs;
	private static final String MY_PREFERS = "MyPrefers";
	
	public AuthSettingsTest(){
		super("org.omich.lang",MainActivity.class);
	}
	
	@Override
	public void setUp(){
		mActivity = getActivity();
		mSettigs = new AuthSettigs(mActivity, MY_PREFERS);
		
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