package com.mycompany.msapp.controllers;

import com.google.gson.Gson;
import com.mycompany.msapp.domain.Message;
import com.mycompany.msapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class UserDataRestController {

    private final UserRepository userRepository;
    private final Gson gson = new Gson();

    public UserDataRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @MessageMapping("/userData")
    @SendTo("/userDataContent/response")
    public Message userData() {
        var messageToSend = new Message(
                "",  //infoContent
                gson.toJson(userRepository.getAllUsers())  //userDataContent
        );
        logger.info("Controller(userData) response message: " + messageToSend);
        return messageToSend;
    }
}
