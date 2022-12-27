package com.uz.shop.animal.world.controlers;

import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping
    public User getUser() {
        return userService.getUserByAuth();
    }
}
