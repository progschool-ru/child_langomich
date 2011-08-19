package org.smdserver.users;

import org.smdserver.util.SmdException;

public interface IUsersStorage
{
	public boolean createUser (String userId, String login, String password);
	public boolean checkPassword (String login, String password);
	public boolean doesLoginExist (String login) throws SmdException;
	public User getUserByLogin (String login);
	public boolean setPassword (String login, String password);
}
