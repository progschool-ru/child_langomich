package org.smdserver.mail;

import javax.mail.PasswordAuthentication;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.smdserver.core.small.ISmdLogger;

public class Mailman implements IMailman
{
	private IMailConfig config;
	private ISmdLogger logger;
	
	public Mailman(IMailConfig config, ISmdLogger logger)
	{
		this.config = config;
		this.logger = logger;
	}
	
	public boolean send(String subject, String text, String to)
	{
		if(!config.isEnabled())
		{
			return false;
		}
		
		try
		{
			Properties props = System.getProperties();
			props.put("mail.smtp.host", config.getSMTPHost());
			props.put("mail.smtp.auth", "true");
			Session session = Session.getDefaultInstance(props, 
					          getAuth(config.getLogin(), config.getPassword()));
			MimeMessage message = new MimeMessage(session);
			message.setSubject(subject);
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setFrom(new InternetAddress(config.getFrom()));
			message.setText(text);
//			message.setHeader("X-Mailer", "Java Mail: ");
			message.setSentDate(new Date());
			Transport.send(message);
			return true;
		}
		catch(MessagingException e)
		{
			log(e);
			return false;
		}		
	}
	
	private void log(Throwable e)
	{
		if(logger != null)
		{
			logger.log(e);
		}
	}
	
	private Authenticator getAuth(final String login, final String password)
	{
		return new Authenticator()
		{
			@Override
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return(new PasswordAuthentication(login, password));
			}
		};
	}
}
