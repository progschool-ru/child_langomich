package org.smdserver.users;

public interface IUsersStorage
{
	public boolean createUser (String dbUserId, String dbLogin, String dirtyPassword);
	public boolean checkPassword (String dbLogin, String dirtyPassword);
	public boolean doesLoginExist (String dbLogin);
	public User getUserByLogin (String dbLogin);
	public void setPassword (String dbLogin, String dirtyPassword) throws Exception;
}
