package com.mycompany.app.controllers;

import com.mycompany.app.domain.User;
import com.mycompany.app.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserDataRestController {

    private final UserRepository userRepository;

    public UserDataRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/userData")
    public List<User> userData() {
        return userRepository.getAllUsers();
    }
}
