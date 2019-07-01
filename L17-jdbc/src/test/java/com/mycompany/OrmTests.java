package com.mycompany;

import com.mycompany.dao.Account;
import com.mycompany.dao.User;
import com.mycompany.executor.JdbcTemplate;
import com.mycompany.executor.JdbcTemplateImpl;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrmTests {

    private static final String URL = "jdbc:h2:mem:";

    private Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);
        connection.setAutoCommit(false);
        createTables(connection);

        return connection;
    }

    private void createTables(Connection connection) throws SQLException {
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

    @Test
    void checkUser() throws SQLException {
        Connection connection = getConnection();

        User user = new User();
        user.setId(1L).setName("Michael").setAge(35);
        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl(connection);

        jdbcTemplate.create(user);
        assertEquals(user, jdbcTemplate.load(1L, User.class),
                "User is not saved or not loaded");

        user.setName("Klaus");
        jdbcTemplate.update(user);
        assertEquals(user, jdbcTemplate.load(1L, User.class),
                "User is not saved or not loaded");

        connection.close();
    }

    @Test
    void checkAccount() throws SQLException {
        Connection connection = getConnection();
        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl(connection);

        Account account = new Account();
        account.setNo(3L).setType("Personal").setRest(500);

        jdbcTemplate.create(account);
        assertEquals(account, jdbcTemplate.load(3L, Account.class),
                "Account is not saved or not loaded");

        account.setRest(450);
        jdbcTemplate.update(account);
        assertEquals(account, jdbcTemplate.load(3L, Account.class),
                "Account is not saved or not loaded");

        connection.close();
    }
}
