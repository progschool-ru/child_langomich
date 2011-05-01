package org.smdserver.users;

import java.security.MessageDigest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class UsersStorage implements IUsersStorage
{
	private Map<String, User> users = new HashMap<String, User>();

	public UsersStorage()
	{
//		users.put("chivorotkiv", new User("1", "chivorotkiv", getMD5Sum("password")));
//		users.put("kkaut", new User("2", "kkauut", getMD5Sum("password")));
	}

	public void setPassword(String login, String password) throws Exception
	{
		if(!users.containsKey(login))
			return;

		users.get(login).setPsw(getPsw(login, password));
	}

	public void addUser(String userId, String login, String psw)
	{
		users.put(login, new User(userId, login, psw));
	}

	public boolean checkPassword(String login, String password)
	{
		return users.containsKey(login) && users.get(login).getPsw().equals(getPsw(login, password));
	}

	public User getUserByLogin(String login)
	{
		if(!users.containsKey(login))
			return null;
		
		return users.get(login);
	}

	public String getPsw(String login, String password)
	{
		return getMD5Sum(login + password);
	}

	protected void iterate(IUsersCallback callback) throws Exception
	{
		Collection<User> entries = users.values();
		for(User user : entries)
		{
			callback.process(user);
		}
	}

	protected String getPswByLogin(String login)
	{
		if(!users.containsKey(login))
			return null;
		
		return users.get(login).getPsw();
	}

	protected void setPswByLogin(String login, String psw)
	{
		if(!users.containsKey(login))
			return;

		users.get(login).setPsw(psw);
	}

	private String getMD5Sum(String password)
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
			e.printStackTrace();
			return password;
		}
	}
}