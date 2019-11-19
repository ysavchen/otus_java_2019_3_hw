package com.mycompany.mutiprocess.database;

import com.mycompany.mutiprocess.database.domain.User;

import java.util.List;

public interface DBService {

    long saveUser(User user);

    List<User> getAllUsers();
}
