package com.uz.shop.animal.world.appuser;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    public static final String EMAIL_ALREADY_TAKEN = "Email already taken!";
    private final AppUserRepository appUserRepository;
    private final static String USER_NOT_FOUND = "User with email %s not found!";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();

        if(userExists) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, EMAIL_ALREADY_TAKEN);
        }

        String encodedPassword = bCryptPasswordEncoder
                .encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);

        //TODO: SEND CONFIRMATION TOKEN 54:16 https://www.youtube.com/watch?v=QwQuro7ekvc

        return "";
    }
}
