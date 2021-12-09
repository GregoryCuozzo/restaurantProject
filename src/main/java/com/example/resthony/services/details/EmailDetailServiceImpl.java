package com.example.resthony.services.details;

import com.example.resthony.services.principal.EmailService;
import com.example.resthony.services.principal.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailDetailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @Override
    public void sendEmail(String email, String object, String text) throws MessagingException, UnsupportedEncodingException, MailSendException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@resthony.com", "Resthony Team");
        helper.setTo(email);
        String subject = object;
        String content = text;
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

}
