package org.smdserver.auth;

enum AnswerKey 
{
	NULL_KEY(null),
	REGISTER_CONFIRM_SENT("register.confirmSent"),
	REGISTER_CONFIRM_SENT_TO_ADMIN("register.confirmSentToAdmin"),
	REGISTER_ACCOUNT_WAS_CREATED("register.accountWasCreated"),
	REGISTER_NOTIFICATION_WASNT_CREATED("register.notificationWasNotSent");
	
	private String key;
	
	private AnswerKey(String key)
	{
		this.key = key;
	}
	
	public String getKey()
	{
		return key;
	}
}
