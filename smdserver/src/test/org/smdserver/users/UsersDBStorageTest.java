package org.smdserver.users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smdserver.db.DbException;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.SmdDB;
import static org.junit.Assert.*;

public class UsersDBStorageTest
{
	private static final String FIRST_LOGIN = "first";
	private static final String FIRST_ID = "1";
	private static final String FIRST_PASSWORD = "firstPassword";

	private static final String DELETE_QUERY = "DELETE FROM smd_users;";

//	Connection connection;
	ISmdDB db;
	ResourceBundle rb;
	private UsersDBStorage instance;

    @Before
    public void setUp () throws Exception
	{
		String testConfig = ResourceBundle.getBundle("org.smdserver.config")
				                      .getString("server.test.properties.file");
		rb = ResourceBundle.getBundle(testConfig);

		db = new SmdDB(rb);


		instance = new UsersDBStorage(db);
		boolean result = instance.createUser(FIRST_ID, FIRST_LOGIN, FIRST_PASSWORD);
		assertTrue(result);
    }

    @After
    public void tearDown () throws Exception
	{
//		connection.close();
		db.close();

		String url = rb.getString("db.url");
		String user = rb.getString("db.user");
		String password = rb.getString("db.password");
		Connection connection = DriverManager.getConnection(url, user, password);
		connection.createStatement().executeUpdate(DELETE_QUERY);
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

	/**
	 * Test of addUser method, of class UsersDBStorage.
	 */
	@Test
	public void testCreate ()
	{
		String login = "second";
		String password = "secondPassword";
		String id = "2";
		String psw = instance.getPsw(login, password);

		boolean success = instance.createUser(id, login, password);
		boolean success2 = instance.createUser(FIRST_ID, login, password);
		boolean success3 = instance.createUser("someOtherId", login, password);

		User user = instance.getUserByLogin(login);

		assertTrue(success);
		assertFalse(success2);
		assertFalse(success3);
		assertEquals("login", login, user.getLogin());
		assertEquals("userId", id, user.getUserId());
		assertEquals("pws", psw, user.getPsw());
	}

	/**
	 * Test of checkPassword method, of class UsersDBStorage.
	 */
	@Test
	public void testCheckPassword ()
	{
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
		String login = FIRST_LOGIN;
		String password = FIRST_PASSWORD;
		String id = FIRST_ID;

		User user = instance.getUserByLogin(login);
		User user2 = instance.getUserByLogin("someLogin");

		assertEquals("login", login, user.getLogin());
		assertEquals("userId", id, user.getUserId());
		assertEquals("pws", instance.getPsw(login, password), user.getPsw());
		assertNull(user2);
	}

	/**
	 * Test of checkPassword method, of class UsersDBStorage.
	 */
	@Test
	public void testDoesUserExists () throws DbException
	{
		String login = FIRST_LOGIN;
		String login2 = "someLogin";

		boolean exists = instance.doesLoginExist(login);
		boolean exists2 = instance.doesLoginExist(login2);

		assertTrue("login exists", exists);
		assertFalse("login doesn't exist", exists2);
	}
}
