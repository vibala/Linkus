package pfe.ece.LinkUS.Controller;


import com.mailjet.client.MailjetClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pfe.ece.LinkUS.Model.MessagesEmail;
import pfe.ece.LinkUS.Service.TokenService.VerificationTokenService;
import pfe.ece.LinkUS.Service.UserEntityService.UserService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;
/**
 * Created by Vignesh on 12/9/2016.
 */
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private static final Logger LOGGER = Logger.getLogger(RegistrationListener.class);

    @Autowired
    private UserService userservice;

    @Autowired
    private VerificationTokenService tokenservice;

    @Autowired
    private MessagesEmail messages;

    private  JavaMailSenderImpl sender;

    private final String usernameFromEmail = "twentyonebala@gmail.com";

    private final String MJ_APIKEY_PUBLIC = "6a3627c75cf706d27c178df62950e436";
    private final String MJ_APIKEY_PRIVATE = "967d30f5c7e40ae3efb0d81905d274e2";
    private MailjetClient client;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        sender = new JavaMailSenderImpl();
       // sender.setHost("smtp-mail.outlook.com");
         sender.setHost("in-v3.mailjet.com");
        sender.setUsername(MJ_APIKEY_PUBLIC);
        //sender.setPassword("n1PyzrC29wF6Yx3f");
        sender.setPassword(MJ_APIKEY_PRIVATE);

        Properties props = new Properties();
        props.put( "mail.smtp.auth", "true" );
        props.put("mail.smtp.port","587");
        props.put("mail.smtp.starttls.required", "true");
        //props.put("mail.smtp.ssl.trust", "smtp-relay.sendinblue.com");
        props.put("mail.smtp.ssl.trust", "in-v3.mailjet.com");
        props.put("mail.smtp.starttls.enable","true");

        sender.setJavaMailProperties(props);

        /*Switch between confirmation registration first step and confirmation registration second step*/
        switch(event.getStep_no()){
            case 1:
                this.confirmRegistrationFirstStep(event);
                break;
            case 2:
                this.confirmRegistrationSecondStep(event);
                break;
        }


    }

    private void confirmRegistrationFirstStep(OnRegistrationCompleteEvent event){
        String token = UUID.randomUUID().toString();
        LOGGER.info("Token value : " + token);
        tokenservice.createVerificationToken(token,event.getRegistred_email());

         /*Preparation du mail*/
        String recipientAddress = event.getRegistred_email();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;

       try{
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            //helper.setFrom("vbala@outlook.fr");
            //helper.setFrom("twenty.lim25@gmail.com");
            helper.setFrom(usernameFromEmail);
            helper.setTo(recipientAddress);
            helper.setSubject(subject);
            helper.setText("Thank you!" + "" + "http://192.168.0.24:9999" + confirmationUrl + "");
            sender.send(message);
        }catch (MessagingException e) {
            e.printStackTrace();
        }

       /* MailjetRequest email;
        JSONArray recipients;
        MailjetResponse response;
        recipients = new JSONArray()
                .put(new JSONObject().put(Contact.EMAIL,recipientAddress));
        email = new MailjetRequest(Email.resource)
                .property(Email.FROMNAME, "support_linkus")
                .property(Email.FROMEMAIL, usernameFromEmail)
                .property(Email.SUBJECT, subject)
                .property(Email.TEXTPART, confirmationUrl)
                .property(Email.RECIPIENTS, recipients)
                .property(Email.MJCUSTOMID, "JAVA-Email");

        try {
            response = client.post(email);
            System.out.println("ZZZZZZZZZ 1: " + response.toString());
        } catch (MailjetException e) {
            System.out.println("MAIL EXCEPTION 1: " + e.getMessage());
            e.printStackTrace();
        } catch (MailjetSocketTimeoutException e) {
            System.out.println("MailjetSocketTimeoutException1 : " + e.getMessage());
            e.printStackTrace();
        }*/
    }

    private void confirmRegistrationSecondStep(OnRegistrationCompleteEvent event){
        /*Preparation du mail*/
        String recipientAddress = event.getRegistred_email();
        String subject = "Registration Confirmation";

        try{
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(usernameFromEmail);
            //helper.setFrom("twenty.lim25@gmail.com");
            helper.setTo(recipientAddress);
            helper.setSubject(subject);
            helper.setText("You receive this email because your are successfully connected");
            sender.send(message);
        }catch (MessagingException e) {
            e.printStackTrace();
        }

       /* MailjetRequest email;
        JSONArray recipients;
        MailjetResponse response;
        recipients = new JSONArray()
                .put(new JSONObject().put(Contact.EMAIL,recipientAddress));
        email = new MailjetRequest(Email.resource)
                .property(Email.FROMNAME, "support_linkus")
                .property(Email.FROMEMAIL, usernameFromEmail)
                .property(Email.SUBJECT, subject)
                .property(Email.TEXTPART, "You receive this email because your are successfully connected")
                .property(Email.RECIPIENTS, recipients)
                .property(Email.MJCUSTOMID, "JAVA-Email");

        try {
            response = client.post(email);
            System.out.println("ZZZZZZZZZ : " + response);
        } catch (MailjetException e) {
            System.out.println("MAIL EXCEPTION : " + e.getMessage());
            e.printStackTrace();
        } catch (MailjetSocketTimeoutException e) {
            System.out.println("MailjetSocketTimeoutException : " + e.getMessage());
            e.printStackTrace();
        }*/


    }


}
