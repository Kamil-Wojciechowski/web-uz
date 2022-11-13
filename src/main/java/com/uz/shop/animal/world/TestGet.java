package com.uz.shop.animal.world;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/test")
public class TestGet {
    @GetMapping
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("Test Working", HttpStatus.OK);
    }

}
