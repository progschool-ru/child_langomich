package org.smdserver.users;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class UsersStorage
{
	private Map<String, String> users = new HashMap<String, String>();

	public UsersStorage()
	{
		users.put("chivorotkiv", getMD5Sum("password"));
		users.put("kkauut", getMD5Sum("password"));
	}

	public void setPassword(String login, String password)
	{
		if(!users.containsKey(login))
			return;

		users.put(login, getMD5Sum(password));
	}

	public boolean checkPassword(String login, String password)
	{
		return users.containsKey(login) && users.get(login).equals(getMD5Sum(password));
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
