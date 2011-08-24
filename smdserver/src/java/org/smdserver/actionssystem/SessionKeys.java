package org.smdserver.actionssystem;

/**
 * Should contain all keys used in HttpSession.
 * 
 * If you use key from this list, you should be sure that you don't break work
 * of other classes.
 */
public class SessionKeys //TODO: (3.low) Превратить это в enum, там, где используется использовать через метод, принимающий экземпляры SessionKeys
{
	/**
	 * Set in LoginAction. Attribute type is String or null.
	 */
	public static final String CURRENT_LOGIN = "currentLogin";//TODO: (3.low) remove this key.
	public static final String CURRENT_USER_ID = "currentUserId";
}
