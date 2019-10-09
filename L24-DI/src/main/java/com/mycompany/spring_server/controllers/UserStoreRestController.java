package com.mycompany.spring_server.controllers;

import com.google.gson.Gson;
import com.mycompany.spring_server.domain.User;
import com.mycompany.spring_server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserStoreRestController {

    private final UserRepository userRepository;
    private final Gson gson = new Gson();

    public UserStoreRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/userStore", method = RequestMethod.POST)
    @ResponseBody
    public String userStore(@RequestBody User user) {
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
