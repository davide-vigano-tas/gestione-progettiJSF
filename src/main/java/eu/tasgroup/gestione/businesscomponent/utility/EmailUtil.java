package eu.tasgroup.gestione.businesscomponent.utility;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {
	public static void sendEmail(String toEmail, String subject, String emailContent) throws MessagingException {
		final String username = "davide.vigano1999@gmail.com";
		//Token
		final String password = "phsx uigy dubm vjsi";
		
		try {
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			
			Session session = Session.getInstance(props, new Authenticator() {
				//Prende lo username  e la password e controlla che siano valide
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("samuelmastro66@gmail.com", "Support Team"));
			
			//Campo TO
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject(subject);

	        message.setContent(emailContent, "text/html");
	        Transport.send(message);
			
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
}
