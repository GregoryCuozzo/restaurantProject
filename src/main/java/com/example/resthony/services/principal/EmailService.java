package com.example.resthony.services.principal;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {
    public void sendEmail(String email, String object, String text) throws MessagingException, UnsupportedEncodingException;
}
