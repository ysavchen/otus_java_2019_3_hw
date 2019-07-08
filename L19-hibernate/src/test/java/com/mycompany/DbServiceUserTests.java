package com.mycompany;

import com.mycompany.dao.Address;
import com.mycompany.dao.Phone;
import com.mycompany.dao.User;
import com.mycompany.dbservice.DbServiceUser;
import com.mycompany.dbservice.DbServiceUserImpl;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DbServiceUserTests {

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
        dbService.saveUser(user);
        assertEquals(user, dbService.getUser(1L).orElse(null),
                "User is not saved or not loaded");
    }

    @Test
    void createAndLoadUserWithPhoneAndAddress() {
        User user = new User().setName("Henrich").setAge(27);

        Phone phone1 = new Phone().setUser(user).setNumber("111");
        Phone phone2 = new Phone().setUser(user).setNumber("222");
        user.setPhoneData(Set.of(phone1, phone2));

        Address address1 = new Address().setUser(user).setStreet("Fasanenstraße 15");
        Address address2 = new Address().setUser(user).setStreet("Grolmanstr. 36");
        user.setAddressData(Set.of(address1, address2));

        DbServiceUser dbService = new DbServiceUserImpl(sessionFactory);
        dbService.saveUser(user);
        assertEquals(user, dbService.getUser(1L).orElse(null),
                "User is not saved or not loaded");
    }

    /*
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
    }*/

    @AfterAll
    static void closeConnection() {
        sessionFactory.close();
    }

}
