package com.uz.shop.animal.world.registration;

import com.uz.shop.animal.world.appuser.AppUser;
import com.uz.shop.animal.world.appuser.AppUserService;
import com.uz.shop.animal.world.appuser.UserType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@AllArgsConstructor
public class RegistrationService {
    private static final String WRONG_FORMAT_EMAIL = "Email have wrong format!";
    private final EmailValidator emailValidator;
    private final AppUserService appUserService;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if(!isValidEmail) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, WRONG_FORMAT_EMAIL);
        }
        return appUserService.signUpUser(new AppUser(
                request.getFirstname(),
                request.getLastname(),
                request.getEmail(),
                UserType.CUSTOMER,
                request.getPassword()
                ));
    }


}
