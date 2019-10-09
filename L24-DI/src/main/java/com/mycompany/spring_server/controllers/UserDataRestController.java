package com.mycompany.spring_server.controllers;

import com.google.gson.Gson;
import com.mycompany.spring_server.domain.User;
import com.mycompany.spring_server.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserDataRestController {

    private final UserRepository userRepository;
    private final Gson gson = new Gson();

    public UserDataRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/userData", method = RequestMethod.GET)
    @ResponseBody
    public String userData() {
        List<User> users = userRepository.getAllUsers();
        return gson.toJson(users);
    }
}
