package org.smdserver.users;

public interface IUsersStorage
{
	public boolean checkPassword (String login, String password);
	public User getUserByLogin (String login);
	public void setPassword (String login, String password) throws Exception;
}
