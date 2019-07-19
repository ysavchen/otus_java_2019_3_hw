package com.mycompany;

import com.mycompany.data.User;

import java.util.Optional;

public interface AppWithCache {

    long addUser(User user);

    Optional<User> getUser(long id);

}
