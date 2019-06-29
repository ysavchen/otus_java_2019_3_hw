package com.mycompany.executor;

import com.mycompany.dao.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

public class JdbcTemplateImpl implements JdbcTemplate {

    private static final String URL = "jdbc:h2:mem:";

    private Connection connection;

    private Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL);
                createTables(connection);
                connection.setAutoCommit(false);
            } catch (SQLException ex) {
                System.out.println("Connection is not created: " + ex.toString());
                throw new ExceptionInInitializerError(ex);
            }
        }
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

    @Override
    public void create(Object objectData) {
        Connection connection = getConnection();

        DbExecutor<User> executor = new DbExecutorImpl<>(connection);
        long userId = executor.insertRecord("insert into user(name) values (?)",
                Collections.singletonList("testUserName"));
        System.out.println("created user:" + userId);
        connection.commit();

        Optional<User> user = executor.selectRecord("select id, name from user where id  = ?", userId, resultSet -> {
            try {
                if (resultSet.next()) {
                    return new User(resultSet.getLong("id"), resultSet.getString("name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
        System.out.println(user);

        connection.close();
    }

    @Override
    public void update(Object objectData) {

    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        return null;
    }
}
