package com.mycompany.spring_server.controllers;

import com.mycompany.spring_server.repository.UserRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    private final UserRepository userRepository;

    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
