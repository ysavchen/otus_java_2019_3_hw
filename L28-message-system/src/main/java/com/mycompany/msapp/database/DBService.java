package com.mycompany.msapp.database;

import com.mycompany.msapp.domain.User;

import java.util.List;

public interface DBService {

    long saveUser(User user);

    List<User> getAllUsers();
}