package org.smdserver.users;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.servlet.ServletContext;

public class UsersFileStorage extends UsersStorage
{
	private final String STORAGE_PATH = "/storage/users.dat";

	ServletContext context;
	
	public UsersFileStorage(ServletContext context)
	{
		super();

		this.context = context;
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(context.getRealPath(STORAGE_PATH)));
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
	public void setPassword(String login, String password) throws Exception
	{
		super.setPassword(login, password);
		store();
	}

	private void store() throws Exception
	{
			SaveUserCallback cb = new SaveUserCallback();
			super.iterate(cb);
			cb.close();
	}

	private class SaveUserCallback implements IUsersCallback
	{
		private BufferedWriter bw;

		SaveUserCallback() throws IOException
		{
			bw = new BufferedWriter(new FileWriter(context.getRealPath(STORAGE_PATH)));
		}
		public void process(User user) throws IOException
		{
			bw.write(user.getUserId() + " " + user.getLogin() + " " + user.getPsw() + "\n");
		}

		void close() throws IOException
		{
			bw.close();
		}
	}
}
