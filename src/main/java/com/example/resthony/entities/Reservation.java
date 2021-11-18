package com.example.resthony.entities;

import com.example.resthony.constants.RoleEnum;
import com.example.resthony.utils.BCryptManagerUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Entity
@Table(name = "reservations")
@Data
@Getter
@Setter
@ToString

public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private int id;

    @NotNull
    @Column(name = "date", nullable = false, unique = true)
    private Date date;

    @NotNull
    @Column(name = "time", nullable = false, unique = true)
    private Time time;

    @NotNull
    @Column(name = "restaurant", nullable = false, unique = true)
    private int restaurant;

    @NotNull
    @Column(name = "client", nullable = false, unique = true)
    private int client;
}
