package com.mycompany.app.repository;

import com.mycompany.app.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    long saveUser(User user);

    Optional<User> getUser(long id);

    List<User> getAllUsers();
}