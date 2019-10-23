package com.mycompany.msapp.controllers;

import com.mycompany.msapp.domain.User;
import com.mycompany.msapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class UsersDataRestController {

    private final UserRepository userRepository;

    public UsersDataRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/usersData")
    public List<User> usersData() {
        return userRepository.getAllUsers();
    }
}
