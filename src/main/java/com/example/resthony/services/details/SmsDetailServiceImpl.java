package com.example.resthony.services.details;


import com.example.resthony.config.TwilioConfiguration;
import com.example.resthony.model.dto.reservation.ReservationOut;
import com.example.resthony.model.dto.restaurant.RestoOut;
import com.example.resthony.model.dto.user.UserOut;
import com.example.resthony.model.entities.SmsRequest;
import com.example.resthony.services.principal.ReservationService;
import com.example.resthony.services.principal.RestoService;
import com.example.resthony.services.principal.SmsService;
import com.example.resthony.services.principal.UserService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Objects;

@Service
public class SmsDetailServiceImpl implements SmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsDetailServiceImpl.class);

    private final TwilioConfiguration twilioConfiguration;

    @Autowired
    public SmsDetailServiceImpl(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }
    @Autowired
    private UserService userService;
    @Autowired
    private RestoService restoService;
    @Autowired
    private ReservationService reservationService;

    @Override
    public String sendSms(SmsRequest smsRequest) {

        try {
            PhoneNumber to = new PhoneNumber(smsRequest.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());
            String message = smsRequest.getMessage();
            MessageCreator creator = Message.creator(to, from, message);
            creator.create();
            LOGGER.info("Send SMS " + smsRequest);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";

    }

    //cron = "0 0 12 * *"
    //Utiliser fixed rate pour tester, le Cron en commentaire ci-dessus permet d'envoyer les mails de rappel tout les jours à midi.
    //Methode rappel SMS fonctionnelle, mise en commentaire pour éviter d'envoyer un SMS a chaque lancement du code :)))

    /*@Scheduled(initialDelay = 1000L, fixedRate = 600000L) //1 SMS toutes les 10 mins ici!!! credit limité donc pas abuser sur les tests :D
    public void rappelSms(){
        List<ReservationOut> reservationOuts = reservationService.getAll();
        try {
            for (ReservationOut reservation : reservationOuts) {
                RestoOut resto = restoService.findByName(reservation.getRestaurant());
                Long rappel = resto.getRappel() * 60 * 1000;
                Long jour = (long) (24 * 60 * 1000);
                String userString = reservation.getUser();
                UserOut user = userService.findByUsername(userString);
                if (Objects.equals(user.getContact(), "sms") && user.getContact() != null) {
                    if (reservation.date.getTime() >= reservation.date.getTime() - rappel && reservation.date.getTime() - rappel >= reservation.date.getTime() - jour) {
                        String smsMessage = "Bonjour monsieur " + user.getFirstname() + ", " +
                                "Merci pour votre réservation chez " + resto.getName() + ", " +
                                "Le " + reservation.getDate() + " à " + reservation.getTime() + " pour " + reservation.getNbcouverts() + " personnes. " +
                                "Pour annuler votre réservation, rendez-vous sur votre compte Resthony en ligne. " ;
                        SmsRequest smsRequest = new SmsRequest(user.getPhone(), smsMessage);
                        sendSms(smsRequest);
                    }
                }
            }
        } catch (Exception e) {
        }

    }*/

}

