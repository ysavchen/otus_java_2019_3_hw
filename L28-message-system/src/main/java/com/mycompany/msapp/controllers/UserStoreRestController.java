package com.mycompany.msapp.controllers;

import com.google.gson.Gson;
import com.mycompany.msapp.domain.User;
import com.mycompany.msapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserStoreRestController {

    private final UserRepository userRepository;
    private final Gson gson = new Gson();

    public UserStoreRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/userStore")
    public String userStore(@RequestBody User user) {
        logger.info("User to store: " + user.toString());
        return gson.toJson(storeUser(user));
    }

    private String storeUser(User user) {
        String message;
        try {
            long id = userRepository.saveUser(user);

            message = "User is saved with id = " + id;
        } catch (Exception ex) {
            logger.error("Error: " + ex);
            message = "User is not saved. \n Error: " + ex.getCause();
        }
        return message;
    }
}
