package com.mycompany;

import com.mycompany.dao.Account;
import com.mycompany.dao.User;
import com.mycompany.template.JdbcTemplate;
import com.mycompany.template.JdbcTemplateImpl;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HibernateTests {

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
        user.setId(1L).setName("Michael").setAge(35);

        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl(sessionFactory);
        jdbcTemplate.create(user);
        assertEquals(user, jdbcTemplate.load(1L, User.class),
                "User is not saved or not loaded");
    }

    @Test
    void createAndLoadAccount() {
        Account account = new Account();
        account.setNo(1L).setType("Personal").setRest(500);

        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl(sessionFactory);
        jdbcTemplate.create(account);
        assertEquals(account, jdbcTemplate.load(1L, Account.class),
                "Account is not saved or not loaded");
    }

    @Test
    void updateUser() {
        User user = new User();
        user.setId(2L).setName("Marcus").setAge(30);

        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl(sessionFactory);
        jdbcTemplate.create(user);

        user.setName("Klaus").setAge(31);
        jdbcTemplate.update(user);
        assertEquals(user, jdbcTemplate.load(2L, User.class),
                "User is not updated");
    }

    @Test
    void updateAccount() {
        Account account = new Account();
        account.setNo(2L).setType("Corporate").setRest(100000);

        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl(sessionFactory);
        jdbcTemplate.create(account);

        account.setRest(50000);
        jdbcTemplate.update(account);
        assertEquals(account, jdbcTemplate.load(2L, Account.class),
                "Account is not updated");
    }

    @Test
    void checkNullFields() {
        User user = new User();
        user.setId(3L).setName(null);

        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl(sessionFactory);
        jdbcTemplate.create(user);
        assertEquals(user, jdbcTemplate.load(3L, User.class),
                "User with name = null is not saved or loaded");

        user.setAge(25);
        jdbcTemplate.update(user);
        assertEquals(user, jdbcTemplate.load(3L, User.class),
                "User with name = null is not updated");
    }

    @Test
    void checkNullObject() {
        User user = null;
        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl(sessionFactory);

        assertThrows(NullPointerException.class,
                () -> jdbcTemplate.create(user));
        assertThrows(NullPointerException.class,
                () -> jdbcTemplate.update(user));
    }

    @Test
    void checkLoadForNonExistingId() {
        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl(sessionFactory);
        assertNull(jdbcTemplate.load(100L, User.class));
    }

    @AfterAll
    static void closeConnection() {
        sessionFactory.close();
    }

}
