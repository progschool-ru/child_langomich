package org.omich.lang.test;

import org.omich.lang.comparators.StringCompare;

import android.test.AndroidTestCase;

public class StringCompareTester extends AndroidTestCase{

	public StringCompareTester(){
		super();
	}
	
	public void test1(){
		String str1 = "a";
		String str2 = "ab";
		
		int result = StringCompare.compare(str1,str2);
		assertEquals(result, 1);
	}
	
	public void test2(){
		String str1 = "a";
		String str2 = "a";
		
		int result = StringCompare.compare(str1,str2);
		assertEquals(result, 0);
	}
	
	public void test3(){
		String str1 = "ab";
		String str2 = "a";
		
		int result = StringCompare.compare(str1,str2);
		assertEquals(result, -1);
	}
	
	public void test4(){
		String str1 = "A";
		String str2 = "a";
		
		int result = StringCompare.compare(str1,str2);
		assertEquals(result, 0);
	}
}
