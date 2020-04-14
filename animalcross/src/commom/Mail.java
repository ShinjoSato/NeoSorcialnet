package commom;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// Reference: https://www.youtube.com/watch?v=A7HAB5whD6I&t=14s
public class Mail {
	
	public static void sendEMail(String sender, String password, String recepient, String subject, String text){
		try {
			Properties properties = new Properties();
		
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.port", "587");
		
			//String sender = "";
			//String password = "";
		
			Session session = Session.getInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(sender, password);
				}
			});
		
			Message message = prepareMessage(session, sender, recepient, subject, text);
			Transport.send(message);
			System.out.println("success!");
		}catch(MessagingException e) {
			e.printStackTrace();
		}
	}
	
	private static Message prepareMessage(Session session, String account, String recepient, String subject, String text) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(account));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject(subject);
			message.setText(text);
			return message;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
