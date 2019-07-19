package com.mycompany;

import com.mycompany.cache.CacheEngine;
import com.mycompany.data.User;
import com.mycompany.dbservice.DbServiceUser;

import java.util.Optional;

public class AppWithCacheImpl implements AppWithCache {

    private final DbServiceUser dbServiceUser;

    private final CacheEngine cacheEngine;

    AppWithCacheImpl(DbServiceUser dbServiceUser, CacheEngine cacheEngine) {
        this.dbServiceUser = dbServiceUser;
        this.cacheEngine = cacheEngine;
    }

    @SuppressWarnings("unchecked")
    @Override
    public long addUser(User user) {
        long id = dbServiceUser.saveUser(user);
        if (id != 0) {
            cacheEngine.put(id, user);
        }
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<User> getUser(long id) {
        User user = (User) cacheEngine.get(id);
        if (user != null) {
            return Optional.of(user);
        }
        return dbServiceUser.getUser(id);
    }
}
