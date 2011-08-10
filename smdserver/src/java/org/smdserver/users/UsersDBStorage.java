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

	private static final String CREATE_USER_QUERY = "INSERT INTO %1$s (user_id, login, psw, email, time_created, time_modified) VALUE (?, ?, ?, \"\", NOW(), NOW());";
	private static final String CHECK_PASSWORD_QUERY = "SELECT login FROM %1$s WHERE login = ? AND psw = ?;";
	private static final String GET_USER_BY_LOGIN_QUERY = "SELECT user_id, login, psw FROM %1$s WHERE login = ?;";
	private static final String SET_PASSWORD_BY_LOGIN_QUERY = "UPDATE %1$s SET psw=? WHERE login = ?;";
	private static final String GET_PSW_BY_ID_QUERY = "SELECT psw FROM %1$s WHERE user_id = ?;";
	private static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM %1$s WHERE user_id = ?;";

	private ISmdDB db;
	private String usersTable;

	public UsersDBStorage(ISmdDB db, String prefix)
	{
		this.db = db;
		this.usersTable = prefix + USERS_TABLE;
	}

	public boolean createUser (String userId, String dirtyLogin, String dirtyPassword)
	{
		String psw = getPsw(dirtyLogin, dirtyPassword);

		try
		{
			ISmdStatement st = createSmdStatement(CREATE_USER_QUERY);
			st.addString(userId);
			st.addString(dirtyLogin);
			st.addString(psw);
			return 1 == db.processSmdStatement(st);
		}
		catch(DbException e)
		{
			return false;
		}
	}

	public boolean checkPassword (String dirtyLogin, String dirtyPassword)
	{
		String psw = getPsw(dirtyLogin, dirtyPassword);
		FirstArgParser parser = new FirstArgParser();

		try
		{
			ISmdStatement st = createSmdStatement(CHECK_PASSWORD_QUERY);
			st.addString(dirtyLogin);
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
		FirstArgParser parser = new FirstArgParser();
		ISmdStatement st = createSmdStatement(GET_USER_BY_LOGIN_QUERY);
		st.addString(dirtyLogin);
		db.selectSingle(st, parser);
		return parser.getValue() != null;
	}

	public User getUserByLogin (String dirtyLogin)
	{
		UserParser parser = new UserParser();
		try
		{
			ISmdStatement st = createSmdStatement(GET_USER_BY_LOGIN_QUERY);
			st.addString(dirtyLogin);
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
		String psw = getPsw(dirtyLogin, password);
		try
		{
			ISmdStatement st = createSmdStatement(SET_PASSWORD_BY_LOGIN_QUERY);
			st.addString(psw);
			st.addString(dirtyLogin);
			return 1 == db.processSmdStatement(st);
		}
		catch(DbException e)
		{
			return false;
		}
	}

	static String getPsw (String login, String password)
	{
		return getMD5Sum(login + password);
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
	
	private ISmdStatement createSmdStatement(String query)
	{
		ISmdStatement st = new SmdStatement();
		st.addQuery(String.format(query, usersTable));
		st.startSet(0);
		return st;
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
}
