package com.example.resthony.services.details;

import com.example.resthony.model.dto.user.CreateUserIn;
import com.example.resthony.model.dto.user.PatchUserIn;
import com.example.resthony.model.dto.user.UserOut;
import com.example.resthony.model.entities.User;
import com.example.resthony.repositories.UserRepository;
import com.example.resthony.services.principal.UserNotFoundException;
import com.example.resthony.services.principal.UserService;
import com.example.resthony.utils.BCryptManagerUtil;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class UsersDetailsServiceImpl implements UserDetailsService, UserService {
    @Autowired
    private UserRepository userRepository;

    public UsersDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username : " + username);
        } else {
            return user;
        }
    }


    @Override
    public UserOut get(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) return null;

        UserOut userOut = convertUserEntityToUserOut(user);

        return userOut;
    }

    @Override
    public List<UserOut> getAll() {
        List<User> userEntities = userRepository.findAll();

        List<UserOut> userOuts = new ArrayList<>();
        for (User user : userEntities) {
            userOuts.add(convertUserEntityToUserOut(user));
        }

        return userOuts;
    }

    @Override
    public UserOut findByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) return null;

        UserOut userOut = convertUserEntityToUserOut(user);
        return userOut;
    }

    @Override
    public int countUser() {
        List<User> userEntities = userRepository.findAll();
        int count = 0;
        for (User user : userEntities) {
            UserOut userOut = convertUserEntityToUserOut(user);
            String role = userOut.roles.toString();
            if (Objects.equals(role, "[USER]")) {
                count += 1;
            }
        }
        return count;
    }


    @Override
    public UserOut create(CreateUserIn createUserIn) {
        User user = convertUserInToUserEntity(createUserIn);
        User newUser = userRepository.save(user);
        return convertUserEntityToUserOut(newUser);
    }


    @Override
    public UserOut register(CreateUserIn createUserIn) {
        User user = convertUserInToUserEntity(createUserIn);
        user.setUserRole();
        User newUser = userRepository.save(user);
        return convertUserEntityToUserOut(newUser);
    }


    @Override
    public UserOut patch(Long id, PatchUserIn patchUserIn) {
        userRepository.updateUser(
                patchUserIn.getUsername(),
                patchUserIn.getLastname(),
                patchUserIn.getFirstname(),
                patchUserIn.getEmail(),
                patchUserIn.getResto(),
                patchUserIn.getPhone(),
                patchUserIn.getContact(),
                id

        );
        User userEntity = userRepository.getById(id);
        return convertUserEntityToUserOut(userEntity);
    }

    @Override
    public UserOut updatePass(Long id, String password) {
        String passwordEncrypt = BCryptManagerUtil.passwordEncoder().encode(password);
        userRepository.updatePass(id, passwordEncrypt);
        User userEntity = userRepository.getById(id);
        String token = userEntity.getResetPasswordToken();
        // On supprime le token du user s'il en a un
        if(token != null) {
            userRepository.updateToken(null, id);
        }
        return convertUserEntityToUserOut(userEntity);
    }

    @Override
    public void delete(Long id) throws NotFoundException {

        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("utilisateur non existant", e);
        }

    }

    @Override
    public void updateUserResto(Long idResto, String username) {
        userRepository.updateResto(idResto, username);
    }


    private UserOut convertUserEntityToUserOut(User user) {

        UserOut userOut = UserOut.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .contact(user.getContact())
                .resto(user.getResto())
                .roles(user.getRoles())
                .build();
        return userOut;
    }


    private User convertUserInToUserEntity(CreateUserIn createUserIn) {
        User user = User.builder()
                .username(createUserIn.getUsername())
                .firstname(createUserIn.getFirstname())
                .lastname(createUserIn.getLastname())
                .email(createUserIn.getEmail())
                .phone(createUserIn.getPhone())
                .contact(createUserIn.getContact())
                .resto(createUserIn.getResto())
                .password(createUserIn.getPassword())
                .roles(createUserIn.getRoles())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(true)
                .credentialsNonExpired(true)
                .build();
        return user;
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByUsername(username);
    }

    public String checkDuplicateCreate(CreateUserIn createUserIn) {
        // Check duplicate
        String message = "";
        for (User user : userRepository.findAll()) {
            if (user.getEmail().equals(createUserIn.getEmail())) {
                message = "Il existe déjà un compte avec cette adresse email, veuillez en choisir une autre.";
                break;
            }
            if (user.getUsername().equals(createUserIn.getUsername())) {
                message = "Ce nom d'utilisateur existe déjà, veuillez en choisir un autre.";
                break;
            }
            if (createUserIn.getContact() != null) {
                if (!createUserIn.getContact().equals("default")) {
                    if (createUserIn.getContact().equals("sms") && createUserIn.getPhone().isEmpty()) {
                        message = "Veuillez entrer un numero de telephone valide si vous avez sélectionné l'option SMS";
                        break;
                    }
                }
            }
        }
        return message;
    }


    public String checkDuplicateUpdate(PatchUserIn patchUserIn) {
        // Check duplicate
        String message = "";
        long idUserPatch = patchUserIn.getId();
        UserOut userPatch = this.get(idUserPatch);

        String usernameUserPatch = userPatch.getUsername();
        String emailUserPatch = userPatch.getEmail();

        for (User user : userRepository.findAll()) {
            if (!emailUserPatch.equals(patchUserIn.getEmail())) {
                if (user.getEmail().equals(patchUserIn.getEmail())) {
                    message = "Il existe déjà un compte avec cette adresse email, veuillez en choisir une autre.";
                }
            }
            if (!usernameUserPatch.equals(patchUserIn.getUsername())) {
                if (user.getUsername().equals(patchUserIn.getUsername())) {
                    message = "Ce nom d'utilisateur existe déjà, veuillez en choisir un autre.";
                }
            }
            if (patchUserIn.getContact() != null) {
                if (!patchUserIn.getContact().equals("default")) {
                    if (patchUserIn.getContact().equals("sms") && patchUserIn.getPhone().isEmpty()) {
                        message = "Veuillez entrer un numero de telephone valide si vous avez sélectionné l'option SMS";
                    }
                }
            }
        }
        return message;
    }

    // Methodes pour reset password - mot de passe oublié

    //Set password token in DB
    @Override
    public void updateResetPassword(String token, String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);


        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("Pas d'utilisateur trouvé avec l'adresse email : " + email);
        }
    }

    //Get by token reset password
    @Override
    public User findByToken(String resetPasswordToken) {
        return userRepository.findByResetPasswordToken(resetPasswordToken);
    }

    //Set updated password

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);

        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

}

