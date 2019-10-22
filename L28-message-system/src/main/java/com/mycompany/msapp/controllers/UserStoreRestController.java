package com.mycompany.msapp.controllers;

import com.google.gson.Gson;
import com.mycompany.msapp.domain.Message;
import com.mycompany.msapp.domain.User;
import com.mycompany.msapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class UserStoreRestController {

    private final UserRepository userRepository;
    private final Gson gson = new Gson();

    public UserStoreRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @MessageMapping("/userStore")
    @SendTo("/infoMessage/response")
    public Message userStore(Message message) {
        logger.info("Controller(userStore) got message: " + message.toString());
        User user = gson.fromJson(message.getUserDataContent(), User.class);
        logger.info("Controller(userStore) user: " + user);

        var messageToSend = new Message(
                storeUser(user),  //infoContent
                gson.toJson(userRepository.getAllUsers())  //userDataContent
        );
        logger.info("Controller(userStore) response message: " + messageToSend);
        return messageToSend;
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
