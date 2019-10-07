package com.mycompany.spring_server.controllers;

import com.mycompany.spring_server.domain.User;
import com.mycompany.spring_server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserRestController {

    private final UserRepository userRepository;

    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/userStore", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse userStore(@RequestBody User user) {
        return new JsonResponse(storeUser(user));
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
