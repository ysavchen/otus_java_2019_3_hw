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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DbServiceUserTests {

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
        user.setName("Michael")
                .setSurname("Schmidt")
                .setAge(35);

        DbServiceUser dbService = new DbServiceUserImpl(sessionFactory);
        long id = dbService.saveUser(user);
        assertEquals(user, dbService.getUser(id).orElse(null),
                "User is not saved or not loaded");
    }

    @Test
    void getAllUsersTest() {
        User user1 = new User()
                .setName("Frank");
        User user2 = new User()
                .setSurname("Lester");
        User user3 = new User()
                .setAge(25);
        User user4 = new User()
                .setName("Alex")
                .setSurname("Schmidt")
                .setAge(30);

        DbServiceUser dbService = new DbServiceUserImpl(sessionFactory);
        dbService.saveUser(user1);
        dbService.saveUser(user2);
        dbService.saveUser(user3);
        dbService.saveUser(user4);

        var users = dbService.getAllUsers();
        assertEquals(4, users.size(),
                "Users are not loaded");
        assertTrue(users.contains(user1), "user1 is not loaded");
        assertTrue(users.contains(user2), "user2 is not loaded");
        assertTrue(users.contains(user3), "user3 is not loaded");
        assertTrue(users.contains(user4), "user4 is not loaded");
    }

    @AfterAll
    static void closeConnection() {
        sessionFactory.close();
    }
}
