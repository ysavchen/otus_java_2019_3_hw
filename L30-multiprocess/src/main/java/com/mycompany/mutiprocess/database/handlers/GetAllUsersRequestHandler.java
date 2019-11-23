package com.mycompany.mutiprocess.database.handlers;

import com.mycompany.mutiprocess.database.domain.User;
import com.mycompany.mutiprocess.database.service.DBService;
import com.mycompany.mutiprocess.ms_client.Message;
import com.mycompany.mutiprocess.ms_client.MessageHandler;
import com.mycompany.mutiprocess.ms_client.MessageType;
import com.mycompany.mutiprocess.ms_client.common.Serializers;

import java.util.List;
import java.util.Optional;

public class GetAllUsersRequestHandler implements MessageHandler {

    private final DBService dbService;

    public GetAllUsersRequestHandler(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        List<User> users = dbService.getAllUsers();
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), Optional.of(msg.getId()), msg.getFromClientId(), MessageType.ALL_USERS_DATA, Serializers.serialize(users)));
    }
}
