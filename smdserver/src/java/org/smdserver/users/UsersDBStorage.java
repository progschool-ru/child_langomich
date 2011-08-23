package org.smdserver.users;

import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.smdserver.db.DbException;
import org.smdserver.db.FirstArgParser;
import org.smdserver.db.IResultParser;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.ISmdStatement;
import org.smdserver.db.SmdStatement;

public class UsersDBStorage implements IUsersStorage
{
	public static final String USERS_TABLE = "users";
	public static final String REGISTRATION_REQUESTS_TABLE = "registration_requests";

	private static final String LOGIN_REGEX = "^[a-zA-Z][\\w-]+";
	private static final String CREATE_USER_QUERY = "INSERT INTO %1$s (user_id, login_key, login, psw, email, about, time_created, time_modified) VALUE (?, ?, ?, ?, ?, ?, NOW(), NOW());";
	private static final String CHECK_PASSWORD_QUERY = "SELECT login FROM %1$s WHERE login_key = ? AND psw = ?;";
	private static final String GET_USER_BY_LOGIN_QUERY = "SELECT user_id, login, psw FROM %1$s WHERE login_key = ?;";
	private static final String SET_PASSWORD_BY_LOGIN_QUERY = "UPDATE %1$s SET psw=? WHERE login_key = ?;";
	private static final String GET_PSW_BY_ID_QUERY = "SELECT psw FROM %1$s WHERE user_id = ?;";
	private static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM %1$s WHERE user_id = ?;";

	private ISmdDB db;
	private String usersTable;
	private String registrationRequestsTable;

	public UsersDBStorage(ISmdDB db)
	{
		this.db = db;
		this.usersTable = db.getTablesPrefix() + USERS_TABLE;
		this.registrationRequestsTable = db.getTablesPrefix() + REGISTRATION_REQUESTS_TABLE;
	}
	
	public boolean createRegistrationRequest(String userId, String dirtyLogin, 
			                   String password,
							   String email, String about)
	{
		return createUserRow(registrationRequestsTable, userId, dirtyLogin, 
				             password, email, about);
	}
	
	public boolean createUser (String userId, String dirtyLogin, 
			                   String password,
							   String email, String about)
	{
		return createUserRow(usersTable, userId, dirtyLogin, password, email, about);
	}

	private boolean createUserRow (String table, String userId, String dirtyLogin, 
			                   String password,
							   String email, String about)
	{
		if(!validateLogin(dirtyLogin))
		{
			return false;
		}
		
		String psw = getPsw(dirtyLogin, password);

		try
		{
			ISmdStatement st = createSmdStatement(CREATE_USER_QUERY, table);
			st.addString(userId);
			st.addString(getLoginKey(dirtyLogin));
			st.addString(dirtyLogin);
			st.addString(psw);
			st.addString(email);
			st.addString(about);
			return 1 == db.processSmdStatement(st);
		}
		catch(DbException e)
		{
			return false;
		}
	}

	public boolean checkPassword (String dirtyLogin, String dirtyPassword)
	{
		if(!validateLogin(dirtyLogin))
		{
			return false;//Can do it without DB request.
		}
		
		String psw = getPsw(dirtyLogin, dirtyPassword);
		FirstArgParser parser = new FirstArgParser();

		try
		{
			ISmdStatement st = createSmdStatement(CHECK_PASSWORD_QUERY);
			st.addString(getLoginKey(dirtyLogin));
			st.addString(psw);
			db.selectSingle(st, parser);
		}
		catch(DbException e)
		{
			return false;
		}

		return parser.getValue() != null;
	}

	public boolean doesLoginExist (String dirtyLogin) throws DbException
	{
		if(!validateLogin(dirtyLogin))
		{
			return false;//Can do it without DB request.
		}
		
		FirstArgParser parser = new FirstArgParser();
		ISmdStatement st = createSmdStatement(GET_USER_BY_LOGIN_QUERY);
		st.addString(getLoginKey(dirtyLogin));
		db.selectSingle(st, parser);
		return parser.getValue() != null;
	}

	public User getUserByLogin (String dirtyLogin)
	{
		if(!validateLogin(dirtyLogin))
		{
			return null;//Can do it without DB request.
		}
		
		UserParser parser = new UserParser();
		try
		{
			ISmdStatement st = createSmdStatement(GET_USER_BY_LOGIN_QUERY);
			st.addString(getLoginKey(dirtyLogin));
			db.selectSingle(st, parser);
		}
		catch(DbException e)
		{
			return null;
		}
		return parser.user;
	}
	
	public boolean setPassword (String dirtyLogin, String password)
	{
		if(!validateLogin(dirtyLogin))
		{
			return false;//Can do it without DB request.
		}
		
		String psw = getPsw(dirtyLogin, password);
		try
		{
			ISmdStatement st = createSmdStatement(SET_PASSWORD_BY_LOGIN_QUERY);
			st.addString(psw);
			st.addString(getLoginKey(dirtyLogin));
			return 1 == db.processSmdStatement(st);
		}
		catch(DbException e)
		{
			return false;
		}
	}

	static String getPsw (String login, String password)
	{
		return getMD5Sum(getLoginKey(login) + password);
	}

	String getPswById (String dbUserId)
	{
		FirstArgParser parser = new FirstArgParser();
		try
		{
			ISmdStatement st = createSmdStatement(GET_PSW_BY_ID_QUERY);
			st.addString(dbUserId);
			db.selectSingle(st, parser);
		}
		catch(DbException e)
		{
			return null;
		}
		return (String)parser.getValue();
	}

	public boolean removeUserById (String dbUserId)
	{
		try
		{
			ISmdStatement st = createSmdStatement(DELETE_USER_BY_ID_QUERY);
			st.addString(dbUserId);
			return 1 == db.processSmdStatement(st);
		}
		catch (DbException e)
		{
			return false;
		}
	}

	private class UserParser implements IResultParser
	{
		User user;

		public boolean parse(ResultSet set) throws SQLException
		{
			String id = set.getString("user_id");
			String login = set.getString("login");
			String psw = set.getString("psw");
			user = new User(id, login, psw);
			return true;
		}
	}

	private ISmdStatement createSmdStatement(String query, String table)
	{
		ISmdStatement st = new SmdStatement();
		st.addQuery(String.format(query, table));
		st.startSet(0);
		return st;
	}
	
	private ISmdStatement createSmdStatement(String query)
	{
		return createSmdStatement(query, usersTable);
	}
	
	private static String getMD5Sum (String password)
	{
		try
		{
			byte[] bytesOfMessage = password.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			StringBuilder sb = new StringBuilder();
			for(byte b : thedigest)
			{
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); //TODO (3.low) use logger
			return password;
		}
	}
	
	private boolean validateLogin(String dirtyLogin)
	{
		return dirtyLogin.matches(LOGIN_REGEX);
	}
	
	private static String getLoginKey(String login)
	{
		return login.toLowerCase().replaceAll("-", "_");
	}
}
