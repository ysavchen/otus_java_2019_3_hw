package com.mycompany.mutiprocess.database;

import com.mycompany.mutiprocess.database.handlers.GetAllUsersRequestHandler;
import com.mycompany.mutiprocess.database.handlers.StoreUserRequestHandler;
import com.mycompany.mutiprocess.database.service.DBService;
import com.mycompany.mutiprocess.database.service.DBServiceImpl;
import com.mycompany.mutiprocess.ms_client.ClientType;
import com.mycompany.mutiprocess.ms_client.MessageType;
import com.mycompany.mutiprocess.ms_client.MsClient;
import com.mycompany.mutiprocess.ms_client.MsClientImpl;
import lombok.SneakyThrows;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.net.Socket;

public class DatabaseClient {

    private static final int PORT = 8081;
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
        MsClient databaseMsClient = new MsClientImpl(ClientType.DATABASE_SERVICE);
        DBService dbService = new DBServiceImpl(sessionFactory());
        databaseMsClient.addHandler(MessageType.STORE_USER, new StoreUserRequestHandler(dbService));
        databaseMsClient.addHandler(MessageType.ALL_USERS_DATA, new GetAllUsersRequestHandler(dbService));
        databaseMsClient.registerMsClient(new Socket(HOST, PORT));
        return dbService;
    }
}
