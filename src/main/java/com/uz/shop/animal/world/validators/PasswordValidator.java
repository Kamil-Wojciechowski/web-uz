package com.uz.shop.animal.world.validators;

import org.springframework.stereotype.Service;


// Prosty walidator, który sprawdza, czy podane hasło jest równy drugiemu
@Service
public class PasswordValidator {
    public boolean test(String password, String confirmedPassword) {
        return password.equals(confirmedPassword);
    }
}
