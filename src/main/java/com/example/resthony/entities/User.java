package com.example.resthony.entities;

import com.example.resthony.constants.RoleEnum;
import com.example.resthony.utils.BCryptManagerUtil;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Cette classe va gérer les utilisateurs de la plateforme
 */

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user")
    private int id;

    @NotNull
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @NotNull
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

    @Column (name="email")
    private String email;

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

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Collection<RoleEnum> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleEnum> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
