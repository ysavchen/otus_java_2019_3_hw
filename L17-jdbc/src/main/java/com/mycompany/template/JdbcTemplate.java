package com.mycompany.template;

public interface JdbcTemplate {

    void create(Object objectData);

    void update(Object objectData);

    <T> T load(long id, Class<T> clazz);

}
