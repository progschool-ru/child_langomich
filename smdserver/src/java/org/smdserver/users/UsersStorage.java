package org.smdserver.users;

import java.security.MessageDigest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class UsersStorage implements IUsersStorage
{
	private Map<String, User> users = new HashMap<String, User>();
        private String ID = "1";

	public void setPassword (String login, String password) throws Exception
	{
		checkUpdated();

		if(!users.containsKey(login))
			return;

		users.get(login).setPsw(getPsw(login, password));
	}
	public boolean checkPassword (String login, String password)
	{
		checkUpdated();
		return users.containsKey(login) && users.get(login).getPsw().equals(getPsw(login, password));
	}
	public boolean checkLogin (String login)
	{
                if(users.get(login) == null)
                    return true;
                else
                    return false;
	}
	public String getID ()
	{
                return ID;
	}
	public User getUserByLogin (String login)
	{
		checkUpdated();

		if(!users.containsKey(login))
			return null;
		
		return users.get(login);
	}

	String getPsw (String login, String password)
	{
		return getMD5Sum(login + password);
	}

	public void createUser (String userId, String login, String password)
	{
		checkUpdated();
		addUser(userId, login, getPsw(login, password));
	}

	protected void addUser (String userId, String login, String psw)
	{
		users.put(login, new User(userId, login, psw));
                if(Integer.valueOf(userId)>=Integer.valueOf(ID))
                    ID = Integer.toString(Integer.valueOf(userId)+1);
	}

	protected void removeUserByLogin (String login)
	{
		users.remove(login);
	}

	protected void iterate (IUsersCallback callback) throws Exception
	{
		Collection<User> entries = users.values();
		for(User user : entries)
		{
			callback.process(user);
		}
	}

	protected String getPswByLogin (String login)
	{
		if(!users.containsKey(login))
			return null;
		
		return users.get(login).getPsw();
	}

	protected void setPswByLogin (String login, String psw)
	{
		if(!users.containsKey(login))
			return;

		users.get(login).setPsw(psw);
	}

	protected void checkUpdated()
	{
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
			e.printStackTrace();
			return password;
		}
	}
}