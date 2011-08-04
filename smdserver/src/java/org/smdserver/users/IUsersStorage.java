package org.smdserver.users;

import org.smdserver.core.SmdException;

public interface IUsersStorage
{
	public boolean createUser (String dbUserId, String dbLogin, String dirtyPassword);
	public boolean checkPassword (String dbLogin, String dirtyPassword);
	public boolean doesLoginExist (String dbLogin) throws SmdException;
	public User getUserByLogin (String dbLogin);
	public boolean setPassword (String dbLogin, String dirtyPassword);
}
