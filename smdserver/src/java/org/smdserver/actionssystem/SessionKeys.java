package org.smdserver.actionssystem;

/**
 * Should contain all keys used in HttpSession.
 * 
 * If you use key from this list, you should be sure that you don't break work
 * of other classes.
 */
public class SessionKeys
{
	/**
	 * Set in LoginAction. Attribute type is String or null.
	 */
	public static final String CURRENT_LOGIN = "currentLogin";
	public static final String LANGUAGES = "languages";
}
