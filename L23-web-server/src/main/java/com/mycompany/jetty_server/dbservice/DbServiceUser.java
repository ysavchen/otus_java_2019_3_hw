package com.mycompany.jetty_server.dbservice;

import com.mycompany.jetty_server.dao.User;

import java.util.List;
import java.util.Optional;

public interface DbServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    List<User> getAllUsers();

}
