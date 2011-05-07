package org.smdserver.users;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UsersFileStorage extends UsersStorage
{
	private String realPath;
	
	public UsersFileStorage (String realPath)
	{
		super();

		this.realPath = realPath;
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(realPath));
			String str;
			while((str = br.readLine()) != null)
			{
				String [] arr = str.split(" ");
				addUser(arr[0], arr[1], arr[2]);
			}
		}
		catch(IOException e)
		{
			
		}
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
//			bw = new BufferedWriter(new FileWriter(context.getRealPath(storagePath)));
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
