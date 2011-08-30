package org.smdserver.users;

import java.sql.Connection;
import java.sql.DriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smdserver.db.DBConfig;
import org.smdserver.core.small.ConsoleSmdLogger;
import org.smdserver.db.DbException;
import org.smdserver.db.IDBConfig;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.SmdDB;
import org.smdserver.core.small.ISmdLogger;
import static org.junit.Assert.*;

public class UsersDBStorageTest
{
	private static final String FIRST_LOGIN = "First";
	private static final String FIRST_LOGIN2 = "fIrst";
	private static final String FIRST_ID = "1";
	private static final String FIRST_PASSWORD = "firstPassword";
	private static final String EMAIL = "some@example.org";
	private static final String ABOUT = "Peter Ivanov";

	private static final String DELETE_QUERY = "DELETE FROM smd_users;";

	ISmdDB db;
	IDBConfig config;
	private UsersDBStorage instance;

    @Before
    public void setUp () throws Exception
	{
		//TODO: (3.low) Create DBConfig in single place in tests
		config = new DBConfig("org.smdserver.config", 
				              "file.server.test.properties");

		ISmdLogger logger = new ConsoleSmdLogger(System.out);
		db = new SmdDB(config, logger);

		instance = new UsersDBStorage(db, logger, "");
		boolean result = instance.createUser(FIRST_ID, FIRST_LOGIN, FIRST_PASSWORD, EMAIL, ABOUT);
		assertTrue(result);
    }

    @After
    public void tearDown () throws Exception
	{
		db.close();

		String url = config.getDBUrl();
		String user = config.getDBUser();
		String password = config.getDBPassword();
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
		helpTestSetPassword(FIRST_LOGIN);
	}

	/**
	 * Test of setPassword method, of class UsersDBStorage.
	 */
	@Test
	public void testSetPasswordCaseInsensitive () throws Exception
	{
		helpTestSetPassword(FIRST_LOGIN2);
	}
	
	/**
	 * Test of createUser method, of class UsersDBStorage.
	 */
	@Test
	public void testCreate ()
	{
		String login = "sECond";
		String login2 = "seconD";
		String password = "secondPassword";
		String id = "2";
		String psw = instance.getPsw(login, password);

		boolean success = instance.createUser(id, login, password, EMAIL, ABOUT);
		boolean success2 = instance.createUser(FIRST_ID, login, password, EMAIL, ABOUT);
		boolean success3 = instance.createUser("someOtherId", login, password, EMAIL, ABOUT);
		boolean success4 = instance.createUser("someOtherId2", login2, password, EMAIL, ABOUT);

		User user = instance.getUserByLogin(login);
		User user2 = instance.getUserByLogin(login2);

		assertTrue(success);
		assertFalse(success2);
		assertFalse(success3);
		assertFalse(success4);
		assertEquals("login", login, user.getLogin());
		assertEquals("userId", id, user.getUserId());
		assertEquals("pws", psw, user.getPsw());
		assertEquals("login case insencitive", login, user2.getLogin());
		assertEquals("userId case insencitive", id, user2.getUserId());
		assertEquals("pws case insencitive", psw, user2.getPsw());
	}
	
	/**
	 * Test of createUser method, of class UsersDBStorage.
	 */
	@Test
	public void testCreateWithIncorrectSymbols ()
	{
		String login = "pетя";
		String id = "2";
		String login2 = "*4(";
		String id2 = "3";
		String login3 = "_mama_12";
		String id3 = "4";
		String login4 = "M4-_";
		String id4 = "5";
		String login5 = "a";
		String id5 = "6";
		String password = "secondPassword";
		String psw = instance.getPsw(login4, password);

		boolean success = instance.createUser(id, login, password, EMAIL, ABOUT);
		boolean success2 = instance.createUser(id2, login2, password, EMAIL, ABOUT);
		boolean success3 = instance.createUser(id3, login3, password, EMAIL, ABOUT);
		boolean success4 = instance.createUser(id4, login4, password, EMAIL, ABOUT);
		boolean success5 = instance.createUser(id5, login5, password, EMAIL, ABOUT);

		User user = instance.getUserByLogin(login);
		User user2 = instance.getUserByLogin(login2);
		User user3 = instance.getUserByLogin(login3);
		User user4 = instance.getUserByLogin(login4);
		User user5 = instance.getUserByLogin(login5);

		assertFalse(success);
		assertFalse(success2);
		assertFalse(success3);
		assertTrue(success4);
		assertFalse(success5);
		assertNull(user);
		assertNull(user2);
		assertNull(user3);
		assertNull(user5);
		assertEquals("login", login4, user4.getLogin());
		assertEquals("userId", id4, user4.getUserId());
		assertEquals("pws", psw, user4.getPsw());
	}	

	/**
	 * Test of checkPassword method, of class UsersDBStorage.
	 */
	@Test
	public void testCheckPassword ()
	{
		String login = FIRST_LOGIN;
		String login2 = FIRST_LOGIN2;
		String password = FIRST_PASSWORD;
		String password2 = "secondPassword";

		assertTrue("correct password", instance.checkPassword(login, password));
		assertFalse("incorrect password", instance.checkPassword(login, password2));
		assertTrue("correct password", instance.checkPassword(login2, password));
		assertFalse("incorrect password", instance.checkPassword(login2, password2));
	}

	/**
	 * Test of getUserByLogin method, of class UsersStorage.
	 */
	@Test
	public void testGetUserByLogin ()
	{
		String login = FIRST_LOGIN;
		String login2 = FIRST_LOGIN2;
		String password = FIRST_PASSWORD;
		String id = FIRST_ID;

		User user = instance.getUserByLogin(login);
		User user2 = instance.getUserByLogin(login2);
		User user3 = instance.getUserByLogin("someLogin");

		assertEquals("login", login, user.getLogin());
		assertEquals("userId", id, user.getUserId());
		assertEquals("pws", instance.getPsw(login, password), user.getPsw());
		assertEquals("login", login, user2.getLogin());
		assertEquals("userId", id, user2.getUserId());
		assertEquals("pws", instance.getPsw(login, password), user2.getPsw());
		assertNull(user3);
	}

	/**
	 * Test of checkPassword method, of class UsersDBStorage.
	 */
	@Test
	public void testDoesUserExists () throws DbException
	{
		String login = FIRST_LOGIN;
		String login1 = FIRST_LOGIN2;
		String login2 = "someLogin";

		boolean exists = instance.doesLoginExist(login);
		boolean exists1 = instance.doesLoginExist(login1);
		boolean exists2 = instance.doesLoginExist(login2);

		assertTrue("login exists", exists);
		assertTrue("login exists", exists1);
		assertFalse("login doesn't exist", exists2);
	}
	
	@Test
	public void testDashIsEqualsUnderscore()
	{
		String password = "1";
		String login = "mamin-sibiryak";
		String id = "mm";
		String login2 = "mamin_sibiryak";
		String id2 = "ms";
		
		boolean success = instance.createUser(id, login, password, EMAIL, ABOUT);
		boolean success2 = instance.createUser(id2, login2, password, EMAIL, ABOUT);
		
		User user = instance.getUserByLogin(login2);
		
		assertTrue(success);
		assertFalse(success2);
		assertEquals(login, user.getLogin());
	}
	
	private void helpTestSetPassword (String login) throws Exception
	{
		String password = FIRST_PASSWORD;
		String id = FIRST_ID;

		String origPsw = instance.getPswById(id);

		String password1 = "firstPassword2";
		boolean success1 = instance.setPassword(login, password1);

		String login2 = "second";
		String password2 = "secondPassword";
		String id2 = "2";
		boolean success2 = instance.setPassword(login2, password2);

		String resultPassword = instance.getPswById(id);

		assertTrue(success1);
		assertFalse(success2);
		
		assertFalse("oldPassword doesn't equal to new password", origPsw.equals(resultPassword));
		assertEquals(instance.getPsw(login, password1), resultPassword);

		assertNull("second user doesn't exist", instance.getPswById(id2));		
	}
}
