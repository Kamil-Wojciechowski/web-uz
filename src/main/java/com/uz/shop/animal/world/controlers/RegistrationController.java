package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.request.RegistrationRequest;
import com.uz.shop.animal.world.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/register")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<ObjectNode> register(@Valid @RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @PostMapping("/{token}")
    public ResponseEntity<ObjectNode> confirm(@PathVariable("token") String token) {
        return registrationService.confirmToken(token);
    }
}
