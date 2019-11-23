package com.mycompany.mutiprocess.database;

import com.mycompany.mutiprocess.database.handlers.GetAllUsersRequestHandler;
import com.mycompany.mutiprocess.database.handlers.StoreUserRequestHandler;
import com.mycompany.mutiprocess.database.service.DBService;
import com.mycompany.mutiprocess.database.service.DBServiceImpl;
import com.mycompany.mutiprocess.ms_client.Message;
import com.mycompany.mutiprocess.ms_client.MessageType;
import com.mycompany.mutiprocess.ms_client.MsClient;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.net.Socket;

public class DBClient {

    private final MsClient msClient;

    DBClient(MsClient msClient, Socket clientSocket) {
        this.msClient = msClient;

        Message registerMsg = msClient.produceMessage(null, null, MessageType.REGISTER_CLIENT);
        msClient.sendMessage(registerMsg, clientSocket);
    }

    private SessionFactory sessionFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        return new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    DBService start() {
        DBService dbService = new DBServiceImpl(sessionFactory());
        msClient.addHandler(MessageType.STORE_USER, new StoreUserRequestHandler(dbService));
        msClient.addHandler(MessageType.ALL_USERS_DATA, new GetAllUsersRequestHandler(dbService));
        return dbService;
    }
}
