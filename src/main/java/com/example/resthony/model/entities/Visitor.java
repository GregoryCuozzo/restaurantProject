package com.example.resthony.model.entities;

import com.example.resthony.constants.RoleEnum;
import com.example.resthony.utils.BCryptManagerUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.sql.Time;
import java.sql.Date;

@Entity
@Table(name = "visitors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Visitor {

    /**
     * Cette classe va gérer les utilisateurs de la plateforme
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_visitor")
    private Long id;

    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @NotNull
    @Column(name = "firstname", nullable = false)
    private String firstname;


        /*@Column(name = "enabled")
        private boolean enabled;*/

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "resto", nullable = true)
    private Restaurant resto;

    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "nbcouverts", nullable = false)
    private Integer nbcouverts;

    @NotNull
    @Column(name = "date", nullable = false)
    private Date date;

    @NotNull
    @Column(name = "time", nullable = false)
    private Time time;

}


