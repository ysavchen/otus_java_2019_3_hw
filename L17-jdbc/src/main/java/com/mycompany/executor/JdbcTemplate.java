package com.mycompany.executor;

import com.mycompany.dao.User;

import java.sql.SQLException;

public interface JdbcTemplate {

    void create(Object objectData) throws SQLException;

    void update(Object objectData);

    User load(long id) throws SQLException;

}
