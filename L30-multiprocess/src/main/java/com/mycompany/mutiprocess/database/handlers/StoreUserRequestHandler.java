package com.mycompany.mutiprocess.database.handlers;

import com.mycompany.mutiprocess.database.service.DBService;
import com.mycompany.mutiprocess.ms_client.domain.User;
import com.mycompany.mutiprocess.ms_client.Message;
import com.mycompany.mutiprocess.ms_client.MessageHandler;
import com.mycompany.mutiprocess.ms_client.MessageType;
import com.mycompany.mutiprocess.ms_client.common.Serializers;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@Slf4j
public class StoreUserRequestHandler implements MessageHandler {

    private final DBService dbService;

    public StoreUserRequestHandler(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg, UUID clientId) {
        User user = Serializers.deserialize(msg.getPayload(), User.class);
        String info = storeUser(user);
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), Optional.of(msg.getId()), clientId, MessageType.STORE_USER, Serializers.serialize(info)));
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
