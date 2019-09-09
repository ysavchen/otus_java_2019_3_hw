package com.mycompany.jetty_server;

import com.mycompany.jetty_server.dao.User;
import com.mycompany.jetty_server.dbservice.DbServiceUser;
import com.mycompany.jetty_server.dbservice.DbServiceUserImpl;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbServiceTests {

    private static SessionFactory sessionFactory;

    @BeforeAll
    static void createConnection() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    @Test
    void createAndLoadUser() {
        User user = new User();
        user.setName("Michael").setAge(35);

        DbServiceUser dbService = new DbServiceUserImpl(sessionFactory);
        long id = dbService.saveUser(user);
        assertEquals(user, dbService.getUser(id).orElse(null),
                "User is not saved or not loaded");
    }

    @Test
    void getAllUsersTest() {
        User user1 = new User()
                .setName("Frank")
                .setSurname("Kenley");
        User user2 = new User()
                .setName("Thomas")
                .setSurname("Lester");

        DbServiceUser dbService = new DbServiceUserImpl(sessionFactory);
        dbService.saveUser(user1);
        dbService.saveUser(user2);
        assertEquals(2, dbService.getAllUsers().size(),
                "Users are not loaded");
    }

    @AfterAll
    static void closeConnection() {
        sessionFactory.close();
    }
}
