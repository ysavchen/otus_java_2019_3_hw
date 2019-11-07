package com.mycompany.msapp.controllers;

import com.google.gson.Gson;
import com.mycompany.msapp.domain.User;
import com.mycompany.msapp.frontend.FrontendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class UserStoreRestController {

    private final FrontendService frontendService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Gson gson = new Gson();

    public UserStoreRestController(FrontendService frontendService,
                                   SimpMessagingTemplate simpMessagingTemplate) {
        this.frontendService = frontendService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/userStore")
    public void userStore(WSMessage message) {
        logger.info("Controller(userStore) got message: " + message.toString());
        User user = gson.fromJson(message.getUsersDataContent(), User.class);
        logger.info("Controller(userStore) user: " + user);

        frontendService.storeUser(user, infoMessage -> {
            var messageToSend = new WSMessage(
                    infoMessage,  //infoContent
                    ""  //userDataContent
            );
            logger.info("Controller(userData) response message: " + messageToSend);
            simpMessagingTemplate.convertAndSend("/infoMessage/response", messageToSend);
        });
    }
}
