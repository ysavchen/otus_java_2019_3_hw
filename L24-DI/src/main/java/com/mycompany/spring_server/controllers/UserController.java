package com.mycompany.spring_server.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class UserController {

    @GetMapping({"/", "/userOperations", "/userOperations.html"})
    public String userOperationsHtml() {
        return "userOperations.html";
    }

}
