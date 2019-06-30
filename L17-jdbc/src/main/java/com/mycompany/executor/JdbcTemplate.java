package com.mycompany.executor;

import java.sql.SQLException;

public interface JdbcTemplate {

    void create(Object objectData) throws SQLException;

    void update(Object objectData);

    <T> T load(long id, Class<T> clazz);

}
