package com.uz.shop.animal.world.validator;

import org.springframework.stereotype.Service;

@Service
public class PasswordValidator {
    public boolean test(String password, String confirmedPassword) {
        return password.equals(confirmedPassword);
    }
}
