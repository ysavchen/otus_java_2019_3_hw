package com.mycompany.mutiprocess.database.handlers;

import com.mycompany.mutiprocess.database.DBService;
import com.mycompany.mutiprocess.database.domain.User;
import com.mycompany.mutiprocess.message_system.Message;
import com.mycompany.mutiprocess.message_system.MessageHandler;
import com.mycompany.mutiprocess.message_system.common.Serializers;

import java.util.List;
import java.util.Optional;

public class GetAllUsersRequestHandler implements MessageHandler {

    private final DBService dbService;
    private static final String RESPONSE_MSG_TYPE = "AllUsersData";

    public GetAllUsersRequestHandler(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        List<User> users = dbService.getAllUsers();
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), Optional.of(msg.getId()), RESPONSE_MSG_TYPE, Serializers.serialize(users)));
    }
}
