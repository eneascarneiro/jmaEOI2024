package com.example.jpa_formacion.service;


import com.example.jpa_formacion.dto.Email;
import freemarker.template.Configuration;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Properties;


@Service
@RequiredArgsConstructor
public class EmailService {

    private Session session;
    


    // MAILTRAP es un servicio que simula un servidor de correo
    // y permite probar y depurar el envío de correos electrónicos
    // sin enviar correos electrónicos reales a los destinatarios.

    public void initSesion() {
        //provide Mailtrap's username
        final String username = "api";
        //provide Mailtrap's password
        final String password = "dd05f45be13af56332d53dec9b2ab3d2";
        //provide Mailtrap's host address
        String host = "live.smtp.mailtrap.io";
        //configure Mailtrap's SMTP server details
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        //create the Session object
        session = Session.getInstance(props,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }


    public void sendMail(Email mail) {

        initSesion();
        try {
            //create a MimeMessage object
            Message message = new MimeMessage(session);
            //set From email field
            System.out.println(mail.getFrom());
            message.setFrom(new InternetAddress(mail.getFrom()));
            //set To email field
            System.out.println(mail.getTo());
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTo()));
            //set email subject field
            System.out.println(mail.getSubject());
            message.setSubject(mail.getSubject());
            //set the content of the email message
            System.out.println(mail.getContent());
            message.setText(mail.getContent());
            //send the email message
            System.out.println("sendMail antes de Transport; " + mail.getContent());
            Transport.send(message);
            System.out.println("Email Message Sent Successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}


