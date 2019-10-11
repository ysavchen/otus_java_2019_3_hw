package com.mycompany.spring_server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping({"/", "/userOperations"})
    public String userOperationsHtml() {
        return "userOperations.html";
    }

}
