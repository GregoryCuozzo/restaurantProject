package com.example.resthony.services.principal;

import com.example.resthony.model.entities.SmsRequest;

public interface SmsService {
    String sendSms(SmsRequest smsRequest);
}
