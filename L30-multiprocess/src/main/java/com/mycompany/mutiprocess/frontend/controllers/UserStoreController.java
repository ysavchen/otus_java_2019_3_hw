package com.mycompany.mutiprocess.frontend.controllers;

import com.google.gson.Gson;
import com.mycompany.mutiprocess.frontend.service.FrontendService;
import com.mycompany.mutiprocess.frontend.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class UserStoreController {

    private final FrontendService frontendService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Gson gson = new Gson();

    public UserStoreController(FrontendService frontendService,
                               SimpMessagingTemplate simpMessagingTemplate) {
        this.frontendService = frontendService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/userStore")
    public void userStore(WSMessage message) {
        logger.info("Controller(userStore) got message: {}", message);
        User user = gson.fromJson(message.getUsersDataContent(), User.class);

        frontendService.storeUser(user, infoMessage -> {
            var messageToSend = new WSMessage(
                    infoMessage,  //infoContent
                    ""  //userDataContent
            );
            logger.debug("Controller(userData) response message: {}", messageToSend);
            simpMessagingTemplate.convertAndSend("/infoMessage/response", messageToSend);
        });
    }
}
