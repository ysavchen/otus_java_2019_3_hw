package com.mycompany.msapp.controllers;

import com.mycompany.msapp.domain.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class NewUserController {

    @MessageMapping("/newUser")
    @SendTo("/newUser/response")
    public Message newUser(Message message) {
        logger.info("Got message: " + message);
        return new Message(message.getNewUserContent());
    }
}
