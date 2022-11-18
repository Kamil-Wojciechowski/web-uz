package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uz.shop.animal.world.services.AuthorizationService;
import com.uz.shop.animal.world.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping(path = "api/v1/token/refresh")
@AllArgsConstructor
public class RefreshController {

    private final UserService userService;

    @PostMapping("/{refreshToken}")
    public void refreshToken(@PathVariable("refreshToken") String refreshToken, HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (refreshToken != null) {
            try {

                Map<String, String> tokens = AuthorizationService.refreshToken(refreshToken, userService);
                response.setContentType("applcation/json");
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception e) {

                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}