package com.mycompany;

import com.mycompany.dao.Account;
import com.mycompany.dao.User;
import com.mycompany.executor.JdbcTemplate;
import com.mycompany.executor.JdbcTemplateImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrmTests {

    private static final String URL = "jdbc:h2:mem:";

    private static Connection connection;

    private static void createTables() throws SQLException {
        PreparedStatement psUser = connection.prepareStatement(
                "create table User(" +
                        "id bigint(20) NOT NULL auto_increment, " +
                        "name varchar(255), " +
                        "age int(3))");
        PreparedStatement psAccount = connection.prepareStatement(
                "create table Account(" +
                        "no bigint(20) NOT NULL auto_increment, " +
                        "type varchar(255), " +
                        "rest number)");

        try (psUser; psAccount) {
            psUser.executeUpdate();
            psAccount.executeUpdate();
        }
    }

    @BeforeAll
    static void createConnection() throws SQLException {
        connection = DriverManager.getConnection(URL);
        connection.setAutoCommit(false);
        createTables();
    }

    @Test
    void createAndLoadUser() {
        User user = new User();
        user.setId(1L).setName("Michael").setAge(35);

        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl(connection);
        jdbcTemplate.create(user);
        assertEquals(user, jdbcTemplate.load(1L, User.class),
                "User is not saved or not loaded");
    }

    @Test
    void createAndLoadAccount() {
        Account account = new Account();
        account.setNo(1L).setType("Personal").setRest(500);

        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl(connection);
        jdbcTemplate.create(account);
        assertEquals(account, jdbcTemplate.load(1L, Account.class),
                "Account is not saved or not loaded");
    }

    @Test
    void updateUser() {
        User user = new User();
        user.setId(2L).setName("Marcus").setAge(30);

        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl(connection);
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

        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl(connection);
        jdbcTemplate.create(account);

        account.setRest(50000);
        jdbcTemplate.update(account);
        assertEquals(account, jdbcTemplate.load(2L, Account.class),
                "Account is not updated");
    }

    @Test
    void checkNull() {
        User user = new User();
        user.setId(3L).setName(null);

        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl(connection);
        jdbcTemplate.create(user);
        assertEquals(user, jdbcTemplate.load(3L, User.class),
                "User with name = null is not saved or loaded");

        user.setAge(25);
        jdbcTemplate.update(user);
        assertEquals(user, jdbcTemplate.load(3L, User.class),
                "User with name = null is not updated");
    }

    @Test
    void checkNoIdFoundException() {

    }

    @Test
    void checkSeveralIdsFoundException() {

    }

    @AfterAll
    static void closeConnection() throws SQLException {
        connection.close();
    }

}
