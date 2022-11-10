package com.uz.shop.animal.world.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/register")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }
    @PostMapping("/{token}")
    public String confirm(@PathVariable("token") String token) {
        return registrationService.confirmToken(token);
    }
}
