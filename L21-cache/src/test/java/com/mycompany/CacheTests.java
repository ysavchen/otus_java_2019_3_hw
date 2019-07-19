package com.mycompany;

import com.mycompany.cache.CacheEngine;
import com.mycompany.cache.CacheEngineImpl;
import com.mycompany.data.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CacheTests {

    @Test
    void checkHitCount() {
        int cacheSize = 10;
        CacheEngine<Long, User> cache = new CacheEngineImpl<>(
                cacheSize, 0, 0, true);
        var users = createUsers(10);
        users.forEach(user -> cache.put(user.getId(), user));
        users.forEach(user -> cache.get(user.getId()));
        assertEquals(10, cache.getHitCount(),
                "Incorrect number of hits");
    }

    @Test
    void checkMissCount() {
        int cacheSize = 10;
        CacheEngine<Long, User> cache = new CacheEngineImpl<>(
                cacheSize, 0, 0, true);
        var users = createUsers(30);
        users.forEach(user -> cache.put(user.getId(), user));
        users.forEach(user -> cache.get(user.getId()));
        assertEquals(20, cache.getMissCount(),
                "Incorrect number of misses");
    }

    private List<User> createUsers(int numUsers) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < numUsers; i++) {

            User user = new User()
                    .setId(i + 1)
                    .setName(RandomStringUtils.randomAlphabetic(5))
                    .setAge(RandomUtils.nextInt());
            users.add(user);
        }
        return users;
    }
}
