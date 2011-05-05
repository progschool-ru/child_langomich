package org.smdserver.users;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UsersStorageTest
{
	private static final String FIRST_LOGIN = "first";
	private static final String FIRST_ID = "1";
	private static final String FIRST_PASSWORD = "firstPassword";
	
	private UsersStorage instance;
	
    @Before
    public void setUp ()
	{
		instance = new UsersStorage();
		instance.addUser(FIRST_ID, FIRST_LOGIN, instance.getPsw(FIRST_LOGIN, FIRST_PASSWORD));
    }

    @After
    public void tearDown ()
	{
		instance = null;
    }

	/**
	 * Test of setPassword method, of class UsersStorage.
	 */
	@Test
	public void testSetPassword () throws Exception
	{
		System.out.println("setPassword");

		String login = FIRST_LOGIN;
		String password = FIRST_PASSWORD;
		
		String origPsw = instance.getPsw(login, password);

		String password1 = "firstPassword2";
		instance.setPassword(login, password1);

		String login2 = "second";
		String password2 = "secondPassword";
		instance.setPassword(login2, password2);

		assertFalse("oldPassword doesn't equal to new password", origPsw.equals(instance.getPswByLogin(login)));
		assertEquals(instance.getPsw(login, password1), instance.getPswByLogin(login));

		assertNull("second user doesn't exist", instance.getUserByLogin(login2));
	}

	/**
	 * Test of addUser method, of class UsersStorage.
	 */
	@Test
	public void testAddUser ()
	{
		System.out.println("addUser");

		String login = "second";
		String psw = "secondPsw";
		String id = "2";

		instance.addUser(id, login, psw);
		
		User user = instance.getUserByLogin(login);

		assertEquals("login", login, user.getLogin());
		assertEquals("userId", id, user.getUserId());
		assertEquals("pws", psw, user.getPsw());
	}

	/**
	 * Test of checkPassword method, of class UsersStorage.
	 */
	@Test
	public void testCheckPassword ()
	{
		System.out.println("checkPassword");

		String login = FIRST_LOGIN;
		String password = FIRST_PASSWORD;
		String password2 = "secondPassword";

		assertTrue("correct password", instance.checkPassword(login, password));
		assertFalse("incorrect password", instance.checkPassword(login, password2));
	}

	/**
	 * Test of getUserByLogin method, of class UsersStorage.
	 */
	@Test
	public void testGetUserByLogin ()
	{
		System.out.println("getUserByLogin");

		String login = FIRST_LOGIN;
		String password = FIRST_PASSWORD;
		String id = FIRST_ID;

		User user = instance.getUserByLogin(login);

		assertEquals("login", login, user.getLogin());
		assertEquals("userId", id, user.getUserId());
		assertEquals("pws", instance.getPsw(login, password), user.getPsw());
	}

	/**
	 * Test of iterate method, of class UsersStorage.
	 */
	@Test
	public void testIterate () throws Exception
	{
		System.out.println("iterate");

		class iterator implements IUsersCallback
		{
			Map<String, String> map = new HashMap<String, String>();
			public void process(User user)
			{
				map.put(user.getUserId(), user.getLogin());
			}
		}

		iterator iter = new iterator();

		String login = "second";
		String password = "secondPassword";
		String id = "2";

		instance.addUser(id, login, password);
		
		instance.iterate(iter);

		assertEquals("elems count in map", 2, iter.map.size());
		assertEquals("first login", FIRST_LOGIN, iter.map.get(FIRST_ID));
		assertEquals("second login", login, iter.map.get(id));
	}

	/**
	 * Test of setPswByLogin method, of class UsersStorage.
	 */
	@Test
	public void testSetPswByLogin ()
	{
		System.out.println("setPswByLogin");

		String login = FIRST_LOGIN;
		String psw = "somePsw";

		instance.setPswByLogin(login, psw);

		assertEquals("psw", psw, instance.getPswByLogin(login));
		assertEquals("psw", psw, instance.getUserByLogin(login).getPsw());
	}

}