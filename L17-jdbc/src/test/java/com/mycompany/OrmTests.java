package com.mycompany;

import com.mycompany.dao.User;
import com.mycompany.executor.JdbcTemplate;
import com.mycompany.executor.JdbcTemplateImpl;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrmTests {

    @Test
    void checkUser() throws SQLException {
        User user = new User();
        user.setId(1L).setName("Michael").setAge(35);
        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl();
        jdbcTemplate.create(user);
        assertEquals(user, jdbcTemplate.load(1L, User.class),
                "User is not saved or not loaded");

        user.setName("Klaus");
        jdbcTemplate.update(user);
        assertEquals(user, jdbcTemplate.load(1L, User.class),
                "User is not saved or not loaded");
    }
}
