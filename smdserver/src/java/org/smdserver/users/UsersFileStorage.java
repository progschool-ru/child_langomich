package org.smdserver.users;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UsersFileStorage extends UsersStorage
{
	private String realPath;
	private long  lastModified;

	public UsersFileStorage (String realPath)
	{
		super();

		this.realPath = realPath;
		readFile();
	}

	@Override
	public void setPassword (String login, String password) throws Exception
	{
		String psw = super.getPswByLogin(login);
		super.setPassword(login, password);
		try
		{
			store();
		}
		catch(Exception e)
		{
			super.setPswByLogin(login, psw);
			throw e;
		}
	}

	@Override
	public void createUser (String userId, String login, String password)
	{
		super.createUser(userId, login, password);
		
		try
		{
			store();
		}
		catch(Exception e)
		{
			removeUserByLogin(login);
		}
	}

	@Override
	public void removeUserByLogin(String login)
	{
		User user = getUserByLogin(login);
		super.removeUserByLogin(login);
		try
		{
			store();
		}
		catch(Exception e)
		{
			super.addUser(user.getUserId(), user.getLogin(), user.getPsw());
		}
	}

	@Override
	protected void checkUpdated()
	{
		File file = new File(realPath);
		if(lastModified < file.lastModified())
			readFile();
	}

	private void readFile()
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(realPath));
			String str;
			while((str = br.readLine()) != null)
			{
				String [] arr = str.split(" ");
				addUser(arr[0], arr[1], arr[2]);
			}

			File file = new File(realPath);
			lastModified = file.lastModified();
		}
		catch(IOException e)
		{

		}
	}

	private void store () throws Exception
	{
		SaveUserCallback cb = new SaveUserCallback();
		super.iterate(cb);
		cb.close();
	}

	private class SaveUserCallback implements IUsersCallback
	{
		private BufferedWriter bw;

		SaveUserCallback () throws IOException
		{
			bw = new BufferedWriter(new FileWriter(realPath));
		}
		public void process (User user) throws IOException
		{
			bw.write(user.getUserId() + " " + user.getLogin() + " " + user.getPsw() + "\n");
		}

		void close () throws IOException
		{
			bw.close();
		}
	}
}
