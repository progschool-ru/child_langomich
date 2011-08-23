package org.smdserver.users;

public class UserEx extends User
{
	private String email;
	private String about;
	
	public UserEx()
	{}
	
	public UserEx (String userId, String login, String psw, String email, String about)
	{
		super(userId, login, psw);
		
		this.email = email;
		this.about = about;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getAbout()
	{
		return about;
	}
	
	public User generateUser()
	{
		return new User(getUserId(), getLogin(), getPsw());
	}
}
