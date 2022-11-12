package com.uz.shop.animal.world.token.recovery;

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
    public ResponseEntity<String> recovery(@PathVariable("email") String email) {
        return new ResponseEntity<>(recoveryService.recovery(email), HttpStatus.CREATED);
    }

    @PostMapping("/token/{token}")
    public ResponseEntity<String> confirm(@PathVariable("token") String token, @Valid @RequestBody RecoveryRequest recoveryRequest) {
        return new ResponseEntity<>(recoveryService.confirmToken(token, recoveryRequest), HttpStatus.OK);
    }

}
