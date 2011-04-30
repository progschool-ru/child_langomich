package org.smdserver.users;

interface IUsersCallback
{
	public void process(User user) throws Exception;
}
