package com.mycompany.spring_server.controllers;

import com.mycompany.spring_server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping({"/", "/userOperations", "/userOperations.html"})
    public String userOperationsHtml() {
        return "userOperations.html";
    }

    @GetMapping("/userOperations.css")
    public String userOperationsCss() {
        return "userOperations.css";
    }

    @GetMapping("/userOperations.js")
    public String userOperationsJs() {
        return "userOperations.js";
    }

}
