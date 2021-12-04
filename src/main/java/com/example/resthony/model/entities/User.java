package com.example.resthony.model.entities;

import com.example.resthony.constants.RoleEnum;
import com.example.resthony.utils.BCryptManagerUtil;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Cette classe va gérer les utilisateurs de la plateforme
 */


@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @NotNull(message = "Pseudo obligatoire")
    @Pattern(regexp = "[A-Za-z0-9]+", message = "Veuillez n'utiliser que des lettres ou des chiffres dans le pseudo")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull(message = "Mot de passe obligatoire")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{10,}$", message = "Le mot de passe doit contenir au moins 10 caratères, une lettre, un chiffre et un caratères spécial" )
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull(message = "Nom obligatoire")
    @Pattern(regexp = "[a-zA-Z]", message = "Veuillez n'utiliser que des lettres dans votre nom")
    @Column(name = "lastname", nullable = false)
    private String lastname;


    @NotNull(message = "Prénom obligatoire")
    @Pattern(regexp = "[a-zA-Z]", message = "Veuillez n'utiliser que des lettres dans votre prénom")
    @Column(name = "firstname", nullable = false)
    private String firstname;


    @ElementCollection(targetClass = RoleEnum.class, fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.REMOVE)
    @JoinTable(
            indexes = {@Index(name = "INDEX_USER_ROLE", columnList = "id_user")},
            name = "roles",
            joinColumns = @JoinColumn(name = "id_user")
    )

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Collection<RoleEnum> roles;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired;

    @Column(name = "enabled")
    private boolean enabled;


    @NotNull(message = "Email obligatoire")
    @Email(message = "Veuillez entrer un email valide")
    @Column (name="email")
    private String email;

    @Column (name = "resto")
    private Integer resto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roles = StringUtils.collectionToCommaDelimitedString(getRoles().stream()
                .map(Enum::name).collect(Collectors.toList()));
        return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
    }

    public void setPassword(String password) {
        if(!password.isEmpty()) {
            this.password = BCryptManagerUtil.passwordEncoder().encode(password);
        }
    }

    public String getUsername(){
        return username;
    }



    public Collection<RoleEnum> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleEnum> roles) {
        this.roles = roles;
    }

    public String getRole(){
        return roles.iterator().next().name();
    }

    public void setUserRole(){
        roles = new ArrayList<>();
        roles.add(RoleEnum.USER);
    }

    }

