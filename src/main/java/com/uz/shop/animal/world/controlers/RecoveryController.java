package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.request.RecoveryRequest;
import com.uz.shop.animal.world.services.RecoveryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/recovery")
@AllArgsConstructor
public class RecoveryController {
    private final RecoveryService recoveryService;

    @PostMapping("/{email}")
    public ResponseEntity<ObjectNode> recovery(@PathVariable("email") String email) {
        return recoveryService.recovery(email);
    }

    @PostMapping("/token/{token}")
    public ResponseEntity<ObjectNode> confirm(@PathVariable("token") String token, @Valid @RequestBody RecoveryRequest recoveryRequest) {
        return recoveryService.confirmToken(token, recoveryRequest);
    }

}
