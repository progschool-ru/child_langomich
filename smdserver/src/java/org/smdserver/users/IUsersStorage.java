package org.smdserver.users;

import org.smdserver.core.small.SmdException;

public interface IUsersStorage
{
	public boolean confirmRegistration(String userId);
	public boolean createRegistrationRequest(String userId, String dirtyLogin, 
			                   String password,
							   String email, String about);
	public boolean createUser (String userId, String dirtyLogin, 
			                   String password,
							   String email, String about);
	public boolean checkPassword (String login, String password);
	public boolean doesLoginExist (String login) throws SmdException;
	public User    getUserByLogin (String login);
	public UserEx  getUserExById (String userId);
	public boolean setPassword (String login, String password);
}
