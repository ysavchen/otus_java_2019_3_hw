package com.mycompany.msapp;

import com.mycompany.msapp.database.DBService;
import com.mycompany.msapp.database.DBServiceImpl;
import com.mycompany.msapp.database.handlers.GetAllUsersRequestHandler;
import com.mycompany.msapp.database.handlers.StoreUserRequestHandler;
import com.mycompany.msapp.frontend.FrontendService;
import com.mycompany.msapp.frontend.FrontendServiceImpl;
import com.mycompany.msapp.frontend.handlers.GetAllUsersResponseHandler;
import com.mycompany.msapp.frontend.handlers.StoreUserResponseHandler;
import com.mycompany.msapp.messageSystem.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageSystemConfig {

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    @Bean
    public SessionFactory sessionFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        return new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public FrontendService frontendService(MessageSystem messageSystem) {
        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem);
        FrontendService frontendService = new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
        frontendMsClient.addHandler(MessageType.STORE_USER, new StoreUserResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.ALL_USERS_DATA, new GetAllUsersResponseHandler(frontendService));
        messageSystem.addClient(frontendMsClient);
        return frontendService;
    }

    @Bean
    public DBService dbService(MessageSystem messageSystem, SessionFactory sessionFactory) {
        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem);
        DBService dbService = new DBServiceImpl(sessionFactory);
        databaseMsClient.addHandler(MessageType.STORE_USER, new StoreUserRequestHandler(dbService));
        databaseMsClient.addHandler(MessageType.ALL_USERS_DATA, new GetAllUsersRequestHandler(dbService));
        messageSystem.addClient(databaseMsClient);
        return dbService;
    }

}
