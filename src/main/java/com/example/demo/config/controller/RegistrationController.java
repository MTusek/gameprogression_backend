package com.example.demo.config.controller;

import com.example.demo.config.model.UserRegistration;
import com.example.demo.config.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody UserRegistration registration) {
        userService.registerUser(registration);
        return ResponseEntity.ok("User registered successfully");
    }
}
