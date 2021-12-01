package com.example.resthony.services.details;

import com.example.resthony.model.dto.reservation.CreateReservationIn;
import com.example.resthony.model.dto.reservation.PatchReservationIn;
import com.example.resthony.model.dto.reservation.ReservationOut;
import com.example.resthony.model.entities.Reservation;
import com.example.resthony.model.entities.User;
import com.example.resthony.repositories.ReservationRepository;
import com.example.resthony.repositories.UserRepository;
import com.example.resthony.services.principal.ReservationService;
import com.example.resthony.services.principal.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationsDetailsServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;


    @Autowired
    public ReservationsDetailsServiceImpl(ReservationRepository reservationRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ReservationOut get(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);

        if (reservation == null) return null;

        ReservationOut reservationOut = convertReservationEntityToReservationOut(reservation);

        return reservationOut;
    }


    @Override
    public List<ReservationOut> getAll() {
        List<Reservation> reservationEntities = reservationRepository.findAll();

        List<ReservationOut> reservationOuts = new ArrayList<>();
        for (Reservation reservation : reservationEntities) {
            reservationOuts.add(convertReservationEntityToReservationOut(reservation));
        }

        return reservationOuts;
    }

    @Override
    public ReservationOut create(CreateReservationIn createReservationIn) {
        Reservation reservation = convertReservationInToReservationEntity(createReservationIn);

        Reservation newReservation = reservationRepository.save(reservation);

        return convertReservationEntityToReservationOut(newReservation);
    }


    @Override
    public ReservationOut patch(Long id, PatchReservationIn patchReservationIn) {

        reservationRepository.updateReservation(
                patchReservationIn.getDate(),
                patchReservationIn.getTime(),
                patchReservationIn.getRestaurant(),
                patchReservationIn.getNbcouverts(),
                patchReservationIn.getUser(),
                patchReservationIn.getAdmin(),
                id
        );

        Reservation reservationEntity = reservationRepository.getById(id);

        return convertReservationEntityToReservationOut(reservationEntity);
    }

    @Override
    public void delete(Long id) throws NotFoundException {

        try {
            reservationRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("No resto found", e);
        }

    }

    private ReservationOut convertReservationEntityToReservationOut(Reservation reservation) {

        ReservationOut reservationOut = ReservationOut.builder()
                .id(reservation.getId())
                .user(reservation.getUser().getUsername())
                .date(reservation.getDate())
                .time(reservation.getTime())
                .restaurant(reservation.getRestaurant())
                .nbcouverts(reservation.getNbcouverts())
                .admin(reservation.getAdmin())
                .build();
        return reservationOut;
    }


    private Reservation convertReservationInToReservationEntity(CreateReservationIn createReservationIn) {
        User user = userRepository.findByUsername(createReservationIn.getUser());
        Reservation reservation = Reservation.builder()
                .user(user)
                .time(createReservationIn.getTime())
                .date(createReservationIn.getDate())
                .restaurant(createReservationIn.getRestaurant())
                .nbcouverts(createReservationIn.getNbcouverts())
                .admin(createReservationIn.getAdmin())
                .build();


        return reservation;
    }


}
