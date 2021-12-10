package com.example.resthony.services.details;

import com.example.resthony.model.dto.reservation.ReservationOut;
import com.example.resthony.model.dto.restaurant.RestoOut;
import com.example.resthony.model.dto.user.UserOut;
import com.example.resthony.services.principal.EmailService;
import com.example.resthony.services.principal.ReservationService;
import com.example.resthony.services.principal.RestoService;
import com.example.resthony.services.principal.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

@Service
@EnableScheduling
public class EmailDetailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;
    @Autowired
    private RestoService restoService;
    @Autowired
    private ReservationService reservationService;

    @Override
    public String sendEmail(String email, String object, String text) throws MessagingException, UnsupportedEncodingException, MailSendException {
        try {


            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("contact@resthony.com", "Resthony Team");
            helper.setTo(email);
            String subject = object;
            String content = text;
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            return "probleme à l'envoi du mail";
        }
        return "";
    }

    //cron = "0 0 12 * *"
    //Utiliser fixed rate pour tester, le Cron en commentaire ci-dessus permet d'envoyer les mails de rappel tout les jours à midi.
    @Scheduled(initialDelay = 1000L, fixedRate = 60000L)
    public void rappelEmail() throws MessagingException, UnsupportedEncodingException, MailSendException, InterruptedException {
        List<ReservationOut> reservationOuts = reservationService.getAll();
        try {
            System.out.println("entré dans le try");
            System.out.println(reservationOuts);
            for (ReservationOut reservation : reservationOuts) {
                System.out.println("entré dans le for");
                RestoOut resto = restoService.findByName(reservation.getRestaurant());
                System.out.println(resto);
                Long rappel = resto.getRappel() * 60 * 1000;
                Long jour = (long) (24 * 60 * 1000);
                String userString = reservation.getUser();
                UserOut user = userService.findByUsername(userString);
                System.out.println(user);
                if (Objects.equals(user.getContact(), "email")) {
                    System.out.println("entré dans IF");
                    System.out.println(rappel);
                    System.out.println(jour);
                    System.out.println(reservation.date.getTime());
                    System.out.println(reservation.date.getTime() - rappel);
                    if (reservation.date.getTime() >= reservation.date.getTime() - rappel && reservation.date.getTime() - rappel >= reservation.date.getTime() - jour) {
                        MimeMessage message = mailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(message);

                        helper.setFrom("contact@resthony.com", "Resthony Team");
                        helper.setTo(user.getEmail());
                        String subject = "Rappel réservation" + resto.getName();
                        String content = "Vous avez réservé une table de " + reservation.getNbcouverts() +
                                " personnes chez " + resto.getName() + " à la date du INCLURE DATE MDR";
                        helper.setSubject(subject);
                        helper.setText(content, true);
                        System.out.println("email envoyé");
                        mailSender.send(message);
                    }
                }
            }
        } catch (Exception e) {
        }

    }



}

@Configuration
@EnableScheduling
class scheduleConfig{

}



