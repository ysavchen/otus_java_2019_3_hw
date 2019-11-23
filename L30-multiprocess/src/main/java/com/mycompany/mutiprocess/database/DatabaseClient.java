package com.mycompany.mutiprocess.database;

import com.mycompany.mutiprocess.database.handlers.GetAllUsersRequestHandler;
import com.mycompany.mutiprocess.database.handlers.StoreUserRequestHandler;
import com.mycompany.mutiprocess.database.service.DBService;
import com.mycompany.mutiprocess.database.service.DBServiceImpl;
import com.mycompany.mutiprocess.ms_client.*;
import lombok.SneakyThrows;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.net.Socket;

public class DatabaseClient {

    private static final int MS_PORT = 8081;
    private static final String HOST = "localhost";

    public static void main(String[] args) {

        new DatabaseClient().start();
    }

    private SessionFactory sessionFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        return new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    @SneakyThrows
    private DBService start() {
        MsClient dbMsClient = new MsClientImpl(ClientType.DATABASE_SERVICE);
        DBService dbService = new DBServiceImpl(sessionFactory());
        dbMsClient.addHandler(MessageType.STORE_USER, new StoreUserRequestHandler(dbService));
        dbMsClient.addHandler(MessageType.ALL_USERS_DATA, new GetAllUsersRequestHandler(dbService));

        Message registerMsg = dbMsClient.produceMessage(null, null, MessageType.REGISTER_CLIENT);
        Socket clientSocket = new Socket(HOST, MS_PORT);
        dbMsClient.sendMessage(registerMsg, clientSocket);

        new DatabaseServer(dbMsClient, clientSocket).start();
        return dbService;
    }
}
