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
import org.smdserver.core.small.ISmdLogger;

public class UsersDBStorage implements IUsersStorage
{
	public static final String USERS_TABLE = "users";
	public static final String REGISTRATION_REQUESTS_TABLE = "registration_requests";

	private static final String LOGIN_REGEX = "^[a-zA-Z][\\w-]+";
	
	private static final String CHECK_PASSWORD_QUERY = "SELECT login FROM %1$s WHERE login_key = ? AND psw = ?;";
	private static final String COPY_USER_FROM_REQUESTS = "INSERT INTO %1$s (user_id, login_key, login, psw, email, about, time_created, time_modified) SELECT user_id, login_key, login, psw, email, about, time_created, NOW() as time_modified FROM %2$s WHERE user_id = ?";
	private static final String CREATE_USER_QUERY = "INSERT INTO %1$s (user_id, login_key, login, psw, email, about, time_created, time_modified) VALUE (?, ?, ?, ?, ?, ?, NOW(), NOW());";
	private static final String GET_PSW_BY_ID_QUERY = "SELECT psw FROM %1$s WHERE user_id = ?;";
	private static final String GET_USER_BY_ID_QUERY = "SELECT user_id, login, psw FROM %1$s WHERE user_id = ?;";
	private static final String GET_USER_EX_BY_ID_QUERY = "SELECT user_id, login, psw, email, about FROM %1$s WHERE user_id = ?;";
	private static final String GET_USER_BY_LOGIN_QUERY = "SELECT user_id, login, psw FROM %1$s WHERE login_key = ?;";
	private static final String SET_PASSWORD_BY_LOGIN_QUERY = "UPDATE %1$s SET psw=? WHERE login_key = ?;";
	private static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM %1$s WHERE user_id = ?;";

	private ISmdDB db;
	private String usersTable;
	private String registrationRequestsTable;
	private ISmdLogger logger;
	private String secret;

	public UsersDBStorage(ISmdDB db, ISmdLogger logger, String secret)
	{
		this.db = db;
		this.logger = logger;
		this.secret = secret;
		
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
			log(e);
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
	
	public UserEx getUserExById (String userId)
	{
		return getUserExById(userId, usersTable);
	}
	
	public UserEx getRegistrationRequestById (String userId)
	{
		return getUserExById(userId, registrationRequestsTable);
	}
	
	public User getUserById (String userId)
	{	
		return getUserByParam(userId, GET_USER_BY_ID_QUERY, usersTable, false);
	}	

	public User getUserByLogin (String dirtyLogin)
	{
		if(!validateLogin(dirtyLogin))
		{
			return null;//Can do it without DB request.
		}
		
		return getUserByParam(getLoginKey(dirtyLogin), GET_USER_BY_LOGIN_QUERY, usersTable, false);
	}
	
	public boolean confirmRegistration(String userId)
	{
		ISmdStatement st = new SmdStatement();
		st.addQuery(String.format(COPY_USER_FROM_REQUESTS, usersTable, registrationRequestsTable));
		st.addQuery(String.format(DELETE_USER_BY_ID_QUERY, registrationRequestsTable));
		st.startSet(0);
		st.addString(userId);
		st.startSet(1);
		st.addString(userId);
		try
		{
			return 2 == db.processSmdStatement(st);
		}
		catch(DbException e)
		{
			log(e);
			return false;
		}
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
			log(e);
			return false;
		}
	}

	String getPsw (String login, String password)
	{
		return getMD5Sum(secret + getLoginKey(login) + password);
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
			log(e);
			return null;
		}
		return (String)parser.getValue();
	}
	
	public boolean removeRegistratioinRequestById (String userId)
	{
		return removeUserById(userId, registrationRequestsTable);
	}

	public boolean removeUserById (String userId)
	{
		return removeUserById(userId, usersTable);
	}

	private class UserParser implements IResultParser
	{
		User user;
		boolean isExtended = false;

		public boolean parse(ResultSet set) throws SQLException
		{
			String id = set.getString("user_id");
			String login = set.getString("login");
			String psw = set.getString("psw");
			
			if(!isExtended)
			{
				user = new User(id, login, psw);
			}
			else
			{
				String email = set.getString("email");
				String about = set.getString("about");
				user = new UserEx(id, login, psw, email, about);
			}
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
	
	private String getMD5Sum (String password)
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
			log(e);
			return password;
		}
	}
	
	private boolean validateLogin(String dirtyLogin)
	{
		return dirtyLogin != null && dirtyLogin.matches(LOGIN_REGEX);
	}
	
	private static String getLoginKey(String login)
	{
		return login.toLowerCase().replaceAll("-", "_");
	}
	
	private void log(Throwable e)
	{
		if(logger != null)
		{
			logger.log(e);
		}
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
			log(e);
			return false;
		}
	}
	
	private UserEx getUserExById (String userId, String table)
	{
		return (UserEx) getUserByParam(userId, GET_USER_EX_BY_ID_QUERY, table, true);
	}

	public boolean removeUserById (String userId, String table)
	{
		try
		{
			ISmdStatement st = createSmdStatement(DELETE_USER_BY_ID_QUERY, table);
			st.addString(userId);
			return 1 == db.processSmdStatement(st);
		}
		catch (DbException e)
		{
			log(e);
			return false;
		}
	}
	
	private User getUserByParam (String param, String template, String table, boolean isExtended)
	{	
		UserParser parser = new UserParser();
		parser.isExtended = isExtended;
		try
		{
			ISmdStatement st = createSmdStatement(template, table);
			st.addString(param);
			db.selectSingle(st, parser);
		}
		catch(DbException e)
		{
			log(e);
			return null;
		}
		return parser.user;
	}	
}
