package com.mycompany.mutiprocess.database;

import com.mycompany.mutiprocess.database.handlers.GetAllUsersRequestHandler;
import com.mycompany.mutiprocess.database.handlers.StoreUserRequestHandler;
import com.mycompany.mutiprocess.database.service.DBService;
import com.mycompany.mutiprocess.ms_client.Message;
import com.mycompany.mutiprocess.ms_client.MessageType;
import com.mycompany.mutiprocess.ms_client.MsClient;

import java.net.Socket;

public class DBClient {

    private final DBService dbService;
    private final MsClient msClient;

    DBClient(DBService dbService, MsClient msClient, Socket clientSocket) {
        this.dbService = dbService;
        this.msClient = msClient;

        Message registerMsg = msClient.produceMessage(null, null, MessageType.REGISTER_CLIENT);
        msClient.sendMessage(registerMsg, clientSocket);
    }

    void start() {
        msClient.addHandler(MessageType.STORE_USER, new StoreUserRequestHandler(dbService));
        msClient.addHandler(MessageType.ALL_USERS_DATA, new GetAllUsersRequestHandler(dbService));
    }
}
