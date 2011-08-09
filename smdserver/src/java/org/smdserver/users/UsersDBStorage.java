package org.smdserver.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.smdserver.db.DbException;
import org.smdserver.db.FirstArgParser;
import org.smdserver.db.IResultParser;
import org.smdserver.db.ISmdDB;

public class UsersDBStorage implements IUsersStorage
{
	public static final String USERS_TABLE = "users";

	private static final String CREATE_USER_QUERY = "INSERT INTO %1$s (user_id, login, psw, email, time_created, time_modified) VALUE (\"%2$s\", \"%3$s\", \"%4$s\", \"\", NOW(), NOW());";
	private static final String CHECK_PASSWORD_QUERY = "SELECT login FROM %1$s WHERE login = \"%2$s\" AND psw = \"%3$s\";";
	private static final String GET_USER_BY_LOGIN_QUERY = "SELECT user_id, login, psw FROM %1$s WHERE login = \"%2$s\";";
	private static final String SET_PASSWORD_BY_LOGIN_QUERY = "UPDATE %1$s SET psw=\"%3$s\" WHERE login = \"%2$s\";";
	private static final String GET_PSW_BY_ID_QUERY = "SELECT psw FROM %1$s WHERE user_id = \"%2$s\";";
	private static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM %1$s WHERE user_id = \"%2$s\";";

	private ISmdDB db;
	private String usersTable;

	public UsersDBStorage(ISmdDB db, String prefix)
	{
		this.db = db;
		this.usersTable = prefix + USERS_TABLE;
	}

	public boolean createUser (String dbUserId, String dirtyLogin, String dirtyPassword)
	{
		String dbLogin = escapeString(dirtyLogin);

		String psw = getPsw(dbLogin, dirtyPassword);

		try
		{
			return db.updateSingle(String.format(CREATE_USER_QUERY, usersTable,
			                  dbUserId, dbLogin, psw));
		}
		catch(DbException e)
		{
			return false;
		}
	}

	public boolean checkPassword (String dirtyLogin, String dirtyPassword)
	{
		String dbLogin = escapeString(dirtyLogin);
		String psw = getPsw(dbLogin, dirtyPassword);
		FirstArgParser parser = new FirstArgParser();

		try
		{
			db.selectSingle(String.format(CHECK_PASSWORD_QUERY, usersTable, dbLogin, psw), parser);
		}
		catch(DbException e)
		{
			return false;
		}

		return parser.getValue() != null;
	}

	public boolean doesLoginExist (String dirtyLogin) throws DbException
	{
		return doesDBLoginExist(escapeString(dirtyLogin));
	}

	public User getUserByLogin (String dirtyLogin)
	{
		String dbLogin = escapeString(dirtyLogin);
		UserParser parser = new UserParser();
		try
		{
			db.selectSingle(String.format(GET_USER_BY_LOGIN_QUERY, usersTable, dbLogin), parser);
		}
		catch(DbException e)
		{
			return null;
		}
		return parser.user;
	}
	
	public boolean setPassword (String dirtyLogin, String password)
	{
		String dbLogin = escapeString(dirtyLogin);
		String psw = getPsw(dbLogin, password);
		String query = String.format(SET_PASSWORD_BY_LOGIN_QUERY, usersTable, dbLogin, psw);
		try
		{
			return db.updateSingle(query);
		}
		catch(DbException e)
		{
			return false;
		}
	}

	static String getPsw (String dbLogin, String dirtyPassword)
	{
		return UsersStorage.getPsw(dbLogin, dirtyPassword);
	}

	String getPswById (String dbUserId)
	{
		FirstArgParser parser = new FirstArgParser();
		try
		{
			db.selectSingle(String.format(GET_PSW_BY_ID_QUERY, usersTable, dbUserId), parser);
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
			return db.updateSingle(String.format(DELETE_USER_BY_ID_QUERY, usersTable, dbUserId));
		}
		catch (DbException e)
		{
			return false;
		}
	}

	private String escapeString(String dirtyValue)
	{
		return db.escapeString(dirtyValue);
	}

	private boolean doesDBLoginExist (String dbLogin) throws DbException
	{
		FirstArgParser parser = new FirstArgParser();
		db.selectSingle(String.format(GET_USER_BY_LOGIN_QUERY, usersTable, dbLogin),parser);
		return parser.getValue() != null;
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
}
