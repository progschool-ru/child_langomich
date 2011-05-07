package org.smdserver.users;

public interface IUsersStorage
{
	public void createUser (String userId, String login, String password);
	public boolean checkPassword (String login, String password);
	public User getUserByLogin (String login);
	public void setPassword (String login, String password) throws Exception;
}
