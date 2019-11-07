package com.mycompany.msapp.database.handlers;

import com.mycompany.msapp.common.Serializers;
import com.mycompany.msapp.database.DBService;
import com.mycompany.msapp.domain.User;
import com.mycompany.msapp.messageSystem.Message;
import com.mycompany.msapp.messageSystem.MessageHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class StoreUserRequestHandler implements MessageHandler {
    private final DBService dbService;
    private static final String RESPONSE_MSG_TYPE = "StoreUser";

    public StoreUserRequestHandler(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        User user = Serializers.deserialize(msg.getPayload(), User.class);
        String info = storeUser(user);
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), Optional.of(msg.getId()), RESPONSE_MSG_TYPE, Serializers.serialize(info)));
    }

    private String storeUser(User user) {
        String message;
        try {
            long id = dbService.saveUser(user);

            message = "User is saved with id = " + id;
        } catch (Exception ex) {
            logger.error("Error: " + ex);
            message = "User is not saved. \n Error: " + ex.getCause();
        }
        return message;
    }
}
