package com.mycompany.mutiprocess.database;

import com.mycompany.mutiprocess.database.handlers.GetAllUsersRequestHandler;
import com.mycompany.mutiprocess.database.handlers.StoreUserRequestHandler;
import com.mycompany.mutiprocess.database.service.DBService;
import com.mycompany.mutiprocess.database.service.DBServiceImpl;
import com.mycompany.mutiprocess.ms_client.*;

import java.net.Socket;
import java.util.Random;

/**
 * Database must be started in a server mode via h2/h2.bat file
 */
public class DBStarter {

    private static final int MS_PORT = 8081;
    private static final String HOST = "localhost";

    public static void main(String[] args) throws Exception {
        DBService dbService = new DBServiceImpl(DBUtils.sessionFactory());

        Socket clientSocket = new Socket(HOST, MS_PORT);
        MsClient msClient = new MsClientImpl(clientSocket, ClientType.DATABASE_SERVICE);
        msClient.addHandler(MessageType.STORE_USER, new StoreUserRequestHandler(dbService));
        msClient.addHandler(MessageType.ALL_USERS_DATA, new GetAllUsersRequestHandler(dbService));

        Message registerMsg = msClient.produceMessage(null, null, MessageType.REGISTER_CLIENT);
        msClient.sendMessage(registerMsg);

        int serverPort = getRandomPort(8085, 8185);
        new DBServer(serverPort, msClient).start();
    }

    private static int getRandomPort(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }
}
