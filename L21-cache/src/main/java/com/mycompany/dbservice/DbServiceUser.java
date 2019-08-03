package com.mycompany.dbservice;

import com.mycompany.cache.CacheEngine;
import com.mycompany.data.User;

import java.util.Optional;

public interface DbServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    void addCache(CacheEngine<Long, User> cache);

    void removeCache();

}