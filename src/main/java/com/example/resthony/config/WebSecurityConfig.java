package com.example.resthony.config;

import com.example.resthony.constants.RoleEnum;
import com.example.resthony.utils.BCryptManagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Gestion de la sécurité de l'application
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * Définition des rôles sous forme de constantes
     */
    private final String ADMIN_ROLE = RoleEnum.ADMIN.name();
    private final String USER_ROLE = RoleEnum.USER.name();
    private final String RESTO_ROLE= RoleEnum.restaurateur.name();

    /**
     * On déclare les services / utilitaires que l'on va utiliser
     */
    private final UserDetailsService userDetailsService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    /**
     * Définition du constructeur avec l'instance des services utilisés
     * @param userDetailsService
     * @param authenticationSuccessHandler
     */
    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService,
                             AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    /**
     * Configuration de la gestion de l'authentification en base
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    @Autowired
    public void configAuthentification(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(BCryptManagerUtil.passwordEncoder());
    }

    /**
     * Règle pour l'authentification
     * On déclare que pour /admin et /user, il faut être authentifié
     * On définit les rôles par pattern d'url
     * On définit que le /* est authorisé à tous
     * On définit la page de login et le succesHandler, qui utilise indirectement le CustomAuthentificationSuccesHandler pour rediriger sur le bon pattern en fonction du rôle
     * On définit les paramètres pour username et password
     * Définition du logout
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**", "/user/**","/restaurateur/**").authenticated()
                .antMatchers("/admin/**").hasAnyAuthority(ADMIN_ROLE)
                .antMatchers("/user/**").hasAuthority(USER_ROLE)
                .antMatchers("/restaurateur/**").hasAnyAuthority(RESTO_ROLE)
                .antMatchers("/*").permitAll()
            .and()
                .formLogin().loginPage("/login").successHandler(authenticationSuccessHandler).failureUrl("/login/error")
                .usernameParameter("username").passwordParameter("password")
            .and()
                .logout().invalidateHttpSession(true)
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .and()
                .csrf()
            .and()
                .sessionManagement().maximumSessions(1).expiredUrl("/login");

    }
}
