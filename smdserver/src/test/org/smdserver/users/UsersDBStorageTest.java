package org.smdserver.users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.SmdDB;
import static org.junit.Assert.*;

public class UsersDBStorageTest
{
	private static final String FIRST_LOGIN = "first";
	private static final String FIRST_ID = "1";
	private static final String FIRST_PASSWORD = "firstPassword";

	private static final String DELETE_QUERY = "DELETE FROM smd_users WHERE user_id = \"" + FIRST_ID + "\";";

	Connection connection;
	private UsersDBStorage instance;

    @Before
    public void setUp () throws Exception
	{
		String testConfig = ResourceBundle.getBundle("org.smdserver.config")
				                      .getString("server.test.properties.file");
		ResourceBundle rb = ResourceBundle.getBundle(testConfig);

		String url = rb.getString("db.url");
		String user = rb.getString("db.user");
		String password = rb.getString("db.password");
		connection = DriverManager.getConnection(url, user, password);
		ISmdDB db = new SmdDB(connection);


		instance = new UsersDBStorage(db);
		boolean result = instance.createUser(FIRST_ID, FIRST_LOGIN, instance.getPsw(FIRST_LOGIN, FIRST_PASSWORD));
		assertTrue(result);
    }

    @After
    public void tearDown () throws Exception
	{
		connection.createStatement().executeUpdate(DELETE_QUERY);
		connection.close();
		instance = null;
    }

	/**
	 * Test of setPassword method, of class UsersDBStorage.
	 */
	@Test
	public void testSetPassword () throws Exception
	{
		String login = FIRST_LOGIN;
		String password = FIRST_PASSWORD;
		String id = FIRST_ID;

		String origPsw = instance.getPsw(login, password);

		String password1 = "firstPassword2";
		instance.setPassword(login, password1);

		String login2 = "second";
		String password2 = "secondPassword";
		String id2 = "2";
		instance.setPassword(login2, password2);

		String resultPassword = instance.getPswById(id);

		assertFalse("oldPassword doesn't equal to new password", origPsw.equals(resultPassword));
		assertEquals(instance.getPsw(login, password1), resultPassword);

		assertNull("second user doesn't exist", instance.getPswById(id2));
	}

//	/**
//	 * Test of addUser method, of class UsersStorage.
//	 */
//	@Test
//	public void testAddUser ()
//	{
//		String login = "second";
//		String psw = "secondPsw";
//		String id = "2";
//
//		instance.addUser(id, login, psw);
//
//		User user = instance.getUserByLogin(login);
//
//		assertEquals("login", login, user.getLogin());
//		assertEquals("userId", id, user.getUserId());
//		assertEquals("pws", psw, user.getPsw());
//	}
//
//	/**
//	 * Test of checkPassword method, of class UsersStorage.
//	 */
//	@Test
//	public void testCheckPassword ()
//	{
//		String login = FIRST_LOGIN;
//		String password = FIRST_PASSWORD;
//		String password2 = "secondPassword";
//
//		assertTrue("correct password", instance.checkPassword(login, password));
//		assertFalse("incorrect password", instance.checkPassword(login, password2));
//	}
//
//	/**
//	 * Test of getUserByLogin method, of class UsersStorage.
//	 */
//	@Test
//	public void testGetUserByLogin ()
//	{
//		String login = FIRST_LOGIN;
//		String password = FIRST_PASSWORD;
//		String id = FIRST_ID;
//
//		User user = instance.getUserByLogin(login);
//
//		assertEquals("login", login, user.getLogin());
//		assertEquals("userId", id, user.getUserId());
//		assertEquals("pws", instance.getPsw(login, password), user.getPsw());
//	}
//
//	/**
//	 * Test of iterate method, of class UsersStorage.
//	 */
//	@Test
//	public void testIterate () throws Exception
//	{
//		class iterator implements IUsersCallback
//		{
//			Map<String, String> map = new HashMap<String, String>();
//			public void process(User user)
//			{
//				map.put(user.getUserId(), user.getLogin());
//			}
//		}
//
//		iterator iter = new iterator();
//
//		String login = "second";
//		String password = "secondPassword";
//		String id = "2";
//
//		instance.addUser(id, login, password);
//
//		instance.iterate(iter);
//
//		assertEquals("elems count in map", 2, iter.map.size());
//		assertEquals("first login", FIRST_LOGIN, iter.map.get(FIRST_ID));
//		assertEquals("second login", login, iter.map.get(id));
//	}
//
//	/**
//	 * Test of setPswByLogin method, of class UsersStorage.
//	 */
//	@Test
//	public void testSetPswByLogin ()
//	{
//		System.out.println("setPswByLogin");
//
//		String login = FIRST_LOGIN;
//		String psw = "somePsw";
//
//		instance.setPswByLogin(login, psw);
//
//		assertEquals("psw", psw, instance.getPswByLogin(login));
//		assertEquals("psw", psw, instance.getUserByLogin(login).getPsw());
//	}
}
