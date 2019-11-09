package com.mycompany.msapp.database.handlers;

import com.mycompany.msapp.common.Serializers;
import com.mycompany.msapp.database.DBService;
import com.mycompany.msapp.domain.User;
import com.mycompany.msapp.messageSystem.Message;
import com.mycompany.msapp.messageSystem.MessageHandler;

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
