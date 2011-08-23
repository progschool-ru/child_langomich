package org.smdserver.auth;

public enum ConfirmationType
{
	ADMIN ("second"),
	MAIL ("first"),
	NONE ("third");
	
	private String value;
	private ConfirmationType(String value)
	{
		this.value = value;
	}
}
