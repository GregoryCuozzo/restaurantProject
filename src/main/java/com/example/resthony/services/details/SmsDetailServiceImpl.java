package com.example.resthony.services.details;


import com.example.resthony.config.TwilioConfiguration;
import com.example.resthony.model.entities.SmsRequest;
import com.example.resthony.services.principal.SmsService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsDetailServiceImpl implements SmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsDetailServiceImpl.class);

    private final TwilioConfiguration twilioConfiguration;

    @Autowired
    public SmsDetailServiceImpl(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

    @Override
    public String sendSms(SmsRequest smsRequest) {

        try {
            PhoneNumber to = new PhoneNumber(smsRequest.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());
            String message = smsRequest.getMessage();
            MessageCreator creator = Message.creator(to, from, message);
            creator.create();
            LOGGER.info("Send SMS "+ smsRequest);
        }
        catch(Exception e){
            return e.getMessage();
        }
        return "";

    }
    //public void sendSms(SmsRequest smsRequest){
    //    createSms(smsRequest);
    //}

}
