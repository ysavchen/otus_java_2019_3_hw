package com.mycompany;

import com.mycompany.dao.User;
import com.mycompany.executor.JdbcTemplate;
import com.mycompany.executor.JdbcTemplateImpl;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class OrmTests {

    @Test
    void checkUser() throws SQLException {
        User user = new User(1L, "Michael", 35);
        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl();
        jdbcTemplate.create(user);
    }
}
