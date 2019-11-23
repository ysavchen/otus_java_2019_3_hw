package com.mycompany.mutiprocess.database.service;

import com.mycompany.mutiprocess.ms_client.domain.User;

import java.util.List;

public interface DBService {

    long saveUser(User user);

    List<User> getAllUsers();
}
