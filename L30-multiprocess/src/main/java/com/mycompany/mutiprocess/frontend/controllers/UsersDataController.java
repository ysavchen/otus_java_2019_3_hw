package com.mycompany.mutiprocess.frontend.controllers;

import com.google.gson.Gson;
import com.mycompany.mutiprocess.frontend.service.FrontendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class UsersDataController {

    private final FrontendService frontendService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Gson gson = new Gson();

    public UsersDataController(FrontendService frontendService,
                               SimpMessagingTemplate simpMessagingTemplate) {
        this.frontendService = frontendService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/usersData")
    public void userData() {
        frontendService.getAllUsers(data -> {
            var messageToSend = new WSMessage(
                    "",  //infoContent
                    gson.toJson(data)  //userDataContent
            );
            logger.debug("Controller(userData) response message: {}", messageToSend);
            simpMessagingTemplate.convertAndSend("/usersDataContent/response", messageToSend);
        });
    }
}
