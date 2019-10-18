package com.mycompany.spring_server.repository;

import com.mycompany.spring_server.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    long saveUser(User user);

    Optional<User> getUser(long id);

    List<User> getAllUsers();
}
