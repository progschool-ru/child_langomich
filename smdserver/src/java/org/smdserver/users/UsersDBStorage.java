package org.smdserver.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.smdserver.db.IResultParser;
import org.smdserver.db.ISmdDB;

public class UsersDBStorage implements IUsersStorage
{
	//TODO: (1. high) get this value from configs
	public static final String USERS_DB = "smd_users";

	private static final String CREATE_USER_QUERY = "INSERT INTO " + USERS_DB + " (user_id, login, psw, email, time_created, time_modified) VALUE (\"%1$s\", \"%2$s\", \"%3$s\", \"\", NOW(), NOW());";
	private static final String CHECK_PASSWORD_QUERY = "SELECT login FROM " + USERS_DB + " WHERE login = \"%1$s\" AND psw = \"%2$s\";";
	private static final String GET_USER_BY_LOGIN_QUERY = "SELECT user_id, login, psw FROM " + USERS_DB + " WHERE login = \"%1$s\";";
	private static final String SET_PASSWORD_BY_LOGIN_QUERY = "UPDATE " + USERS_DB + " SET psw=\"%2$s\" WHERE login = \"%1$s\";";
	private static final String GET_PSW_BY_ID_QUERY = "SELECT psw FROM " + USERS_DB + " WHERE user_id = \"%1$s\";";

	private ISmdDB db;

	public UsersDBStorage(ISmdDB db)
	{
		this.db = db;
	}

	public boolean createUser (String dbUserId, String dirtyLogin, String dirtyPassword)
	{
		String dbLogin = escapeString(dirtyLogin);

		String psw = getPsw(dbLogin, dirtyPassword);

		return db.updateSingle(String.format(CREATE_USER_QUERY,
			                  dbUserId, dbLogin, psw));
	}

	public boolean checkPassword (String dirtyLogin, String dirtyPassword)
	{
		String dbLogin = escapeString(dirtyLogin);
		String psw = getPsw(dbLogin, dirtyPassword);
		FirstArgParser parser = new FirstArgParser();
		db.selectSingle(String.format(CHECK_PASSWORD_QUERY, dbLogin, psw), parser);

		return parser.value != null;
	}

	public boolean doesLoginExist (String dirtyLogin)
	{
		return doesDBLoginExist(escapeString(dirtyLogin));
	}

	public User getUserByLogin (String dirtyLogin)
	{
		String dbLogin = escapeString(dirtyLogin);
		UserParser parser = new UserParser();
		db.selectSingle(String.format(GET_USER_BY_LOGIN_QUERY, dbLogin), parser);
		return parser.user;
	}
	
	public void setPassword (String dirtyLogin, String password)
	{
		String dbLogin = escapeString(dirtyLogin);
		String psw = getPsw(dbLogin, password);
		String query = String.format(SET_PASSWORD_BY_LOGIN_QUERY, dbLogin, psw);
		db.updateSingle(query);
	}

	static String getPsw (String dbLogin, String dirtyPassword)
	{
		return UsersStorage.getPsw(dbLogin, dirtyPassword);
	}

	String getPswById (String dbUserId)
	{
		FirstArgParser parser = new FirstArgParser();
		db.selectSingle(String.format(GET_PSW_BY_ID_QUERY, dbUserId), parser);
		return (String)parser.value;
	}

	private String escapeString(String dirtyValue)
	{
		return db.escapeString(dirtyValue);
	}

	private boolean doesDBLoginExist (String dbLogin)
	{
		FirstArgParser parser = new FirstArgParser();
		db.selectSingle(String.format(GET_USER_BY_LOGIN_QUERY, dbLogin),parser);
		return parser.value != null;
	}

	private class FirstArgParser implements IResultParser
	{
		Object value;

		public boolean parse(ResultSet set) throws SQLException
		{
			value = set.getObject(1);
			return true;
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
}
