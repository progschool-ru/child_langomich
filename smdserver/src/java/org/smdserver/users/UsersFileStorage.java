package org.smdserver.users;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.servlet.ServletContext;

public class UsersFileStorage extends UsersStorage
{
	private ServletContext context;
	private String storagePath;
	
	public UsersFileStorage (ServletContext context, String storagePath)
	{
		super();

		this.context = context;
		this.storagePath = storagePath;
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(context.getRealPath(storagePath)));
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
			bw = new BufferedWriter(new FileWriter(context.getRealPath(storagePath)));
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
