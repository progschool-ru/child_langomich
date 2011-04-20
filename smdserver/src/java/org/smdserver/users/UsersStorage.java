package org.smdserver.users;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class UsersStorage
{
	private Map<String, User> users = new HashMap<String, User>();

	public UsersStorage()
	{
		users.put("chivorotkiv", new User("1", "chivorotkiv", getMD5Sum("password")));
		users.put("kkaut", new User("2", "kkauut", getMD5Sum("password")));
	}

	public void setPassword(String login, String password)
	{
		if(!users.containsKey(login))
			return;

		users.get(login).setPsw(getMD5Sum(password));
	}

	public boolean checkPassword(String login, String password)
	{
		return users.containsKey(login) && users.get(login).getPsw().equals(getMD5Sum(password));
	}

	public User getUserByLogin(String login)
	{
		if(!users.containsKey(login))
			return null;
		
		return users.get(login);
	}

	private String getMD5Sum(String password)
	{
		try
		{
			byte[] bytesOfMessage = password.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			return new String(thedigest);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return password;
		}
	}
}
