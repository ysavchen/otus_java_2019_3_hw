package com.mycompany;

import com.mycompany.dao.Address;
import com.mycompany.dao.Phone;
import com.mycompany.dao.User;
import com.mycompany.dbservice.DbServiceUser;
import com.mycompany.dbservice.DbServiceUserImpl;
import com.mycompany.exceptions.NoDataFoundException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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
        long id = dbService.saveUser(user);
        assertEquals(user, dbService.getUser(id).orElse(null),
                "User is not saved or not loaded");
    }

    @Test
    void createAndLoadUserWithPhoneAndAddress() {
        User user = new User().setName("Henrich").setAge(27);

        Phone phone1 = new Phone().setUser(user).setNumber("111");
        Phone phone2 = new Phone().setUser(user).setNumber("222");
        user.setPhoneData(Set.of(phone1, phone2));

        DbServiceUser dbService = new DbServiceUserImpl(sessionFactory);
        long id = dbService.saveUser(user);
        User userFromDb = dbService.getUser(id)
                .orElseThrow(() -> new NoDataFoundException("User with id = " + id + " is not found"));
        assertEquals(user, userFromDb,
                "User is not saved or not loaded");
        assertEquals(2, userFromDb.getPhoneData().size(),
                "Phone data is not loaded");
    }

    @Test
    void createAndLoadUserWithAddresses() {
        User user = new User().setName("Reiner").setAge(30);

        Address address1 = new Address().setUser(user).setStreet("Fasanenstraße 15");
        Address address2 = new Address().setUser(user).setStreet("Grolmanstr. 36");
        user.setAddressData(Set.of(address1, address2));

        DbServiceUser dbService = new DbServiceUserImpl(sessionFactory);
        long id = dbService.saveUser(user);
        User userFromDb = dbService.getUser(id)
                .orElseThrow(() -> new NoDataFoundException("User with id = " + id + " is not found"));
        assertEquals(2, userFromDb.getAddressData().size(),
                "Address data is not loaded");
    }

    @Test
    void checkNullFields() {
        User user = new User();
        user.setName(null);

        DbServiceUser dbService = new DbServiceUserImpl(sessionFactory);
        long id = dbService.saveUser(user);
        User userFromDb = dbService.getUser(id)
                .orElseThrow(() -> new NoDataFoundException("User with id = " + id + " is not found"));
        assertNull(userFromDb.getName(),
                "User with name = null is not saved or loaded");
    }

    @Test
    void checkLoadForNonExistingId() {
        DbServiceUser dbService = new DbServiceUserImpl(sessionFactory);
        assertTrue(dbService.getUser(100L).isEmpty());
    }

    @AfterAll
    static void closeConnection() {
        sessionFactory.close();
    }

}
