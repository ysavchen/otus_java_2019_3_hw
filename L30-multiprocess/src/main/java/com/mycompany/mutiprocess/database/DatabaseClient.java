package com.mycompany.mutiprocess.database;

import com.mycompany.mutiprocess.database.handlers.GetAllUsersRequestHandler;
import com.mycompany.mutiprocess.database.handlers.StoreUserRequestHandler;
import com.mycompany.mutiprocess.database.service.DBService;
import com.mycompany.mutiprocess.database.service.DBServiceImpl;
import com.mycompany.mutiprocess.ms_client.ClientType;
import com.mycompany.mutiprocess.ms_client.MessageType;
import com.mycompany.mutiprocess.ms_client.MsClient;
import com.mycompany.mutiprocess.ms_client.MsClientImpl;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DatabaseClient {

    public static void main(String[] args) {

    }

    public SessionFactory sessionFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        return new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    public DBService dbService(SessionFactory sessionFactory) {
        MsClient databaseMsClient = new MsClientImpl(ClientType.DATABASE_SERVICE);
        DBService dbService = new DBServiceImpl(sessionFactory);
        databaseMsClient.addHandler(MessageType.STORE_USER, new StoreUserRequestHandler(dbService));
        databaseMsClient.addHandler(MessageType.ALL_USERS_DATA, new GetAllUsersRequestHandler(dbService));
        return dbService;
    }
}
