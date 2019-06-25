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

    @Override
    public void create(Object objectData) throws SQLException {
        Connection connection = getConnection();
        createTable(connection);

        DbExecutor<User> executor = new DbExecutorImpl<>(connection);
        long userId = executor.insertRecord("insert into user(name) values (?)", Collections.singletonList("testUserName"));
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
    public void createOrUpdate(Object objectData) {

    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        return null;
    }

    private Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);
        connection.setAutoCommit(false);
        return connection;
    }

    private void createTable(Connection connection) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement("create table user(id long auto_increment, name varchar(50))")) {
            pst.executeUpdate();
        }
    }
}
