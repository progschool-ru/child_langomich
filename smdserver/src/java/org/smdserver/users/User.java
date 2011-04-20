package org.smdserver.users;

public class User
{
	private String userId;
	private String login;
	private String psw;

	public User()
	{}

	public User(String userId, String login, String psw) {
		this.userId = userId;
		this.login = login;
		this.psw = psw;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	String getPsw() {
		return psw;
	}

	void setPsw(String psw) {
		this.psw = psw;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
