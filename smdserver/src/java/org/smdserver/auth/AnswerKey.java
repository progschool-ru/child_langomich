package org.smdserver.auth;

enum AnswerKey 
{
	NULL_KEY(null),
	REGISTER_CONFIRM_SENT(Messages.REGISTER_CONFIRM_SENT),
	REGISTER_CONFIRM_SENT_TO_ADMIN(Messages.REGISTER_CONFIRM_SENT_TO_ADMIN),
	REGISTER_ACCOUNT_WAS_CREATED(Messages.REGISTER_ACCOUNT_WAS_CREATED),
	REGISTER_NOTIFICATION_WASNT_CREATED(Messages.REGISTER_NOTIFICATION_WASNT_CREATED);
	
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
