package com.mycompany.dbservice;

import com.mycompany.dao.User;

import java.util.Optional;

public interface DbServiceUser {

    Long saveUser(User user);

    Optional<User> getUser(long id);

}
