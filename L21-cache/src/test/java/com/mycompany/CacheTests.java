package com.mycompany;

import com.mycompany.cache.CacheEngine;
import com.mycompany.cache.CacheEngineImpl;
import com.mycompany.data.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CacheTests {

    @Test
    void checkHitCount() {
        int cacheSize = 10;
        int putElements = 10;
        CacheEngine<Long, User> cache = new CacheEngineImpl<>(
                cacheSize, 0, 0, true);
        var users = createUsers(putElements);
        users.forEach(user -> cache.put(user.getId(), user));
        users.forEach(user -> cache.get(user.getId()));
        assertEquals(putElements, cache.getHitCount(),
                "Incorrect number of hits");
    }

    @Test
    void checkMissCount() {
        int cacheSize = 10;
        int putElements = 30;
        CacheEngine<Long, User> cache = new CacheEngineImpl<>(
                cacheSize, 0, 0, true);
        var users = createUsers(putElements);
        users.forEach(user -> cache.put(user.getId(), user));
        users.forEach(user -> cache.get(user.getId()));
        assertEquals(20, cache.getMissCount(),
                "Incorrect number of misses");
    }

    @Test
    void checkLifeTime() throws InterruptedException {
        int cacheSize = 10;
        int putElements = 10;
        CacheEngine<Long, User> cache = new CacheEngineImpl<>(
                cacheSize, 500, 0, false);
        var users = createUsers(putElements);
        users.forEach(user -> cache.put(user.getId(), user));

        Thread.sleep(600);
        users.forEach(user -> cache.get(user.getId()));
        assertEquals(putElements, cache.getMissCount(),
                "Incorrect number of misses for lifetime check");
    }

    @Test
    void checkLifeTimeWithEternalTrue() throws InterruptedException {
        int cacheSize = 10;
        int putElements = 10;
        CacheEngine<Long, User> cache = new CacheEngineImpl<>(
                cacheSize, 500, 0, true);
        var users = createUsers(putElements);
        users.forEach(user -> cache.put(user.getId(), user));

        Thread.sleep(600);
        users.forEach(user -> cache.get(user.getId()));
        assertEquals(putElements, cache.getHitCount(),
                "Incorrect number of misses for lifetime check");
    }

    @Test
    void checkIdleTime() throws InterruptedException {
        int cacheSize = 10;
        int putElements = 10;
        CacheEngine<Long, User> cache = new CacheEngineImpl<>(
                cacheSize, 0, 500, false);
        var users = createUsers(putElements);
        users.forEach(user -> cache.put(user.getId(), user));

        //check elements' removal is postponed after a hit
        for (int i = 0; i < 3; i++) {
            Thread.sleep(300);
            users.forEach(user -> cache.get(user.getId()));
            assertTrue(cache.getHitCount() > 0,
                    "Incorrect number of hits for idle time check");
        }
        //check elements are removed
        Thread.sleep(600);
        users.forEach(user -> cache.get(user.getId()));
        assertEquals(putElements, cache.getMissCount(),
                "Incorrect number of misses for idle time check");
    }

    @Test
    void checkIdleTimeWithEternalTrue() throws InterruptedException {
        int cacheSize = 10;
        int putElements = 10;
        CacheEngine<Long, User> cache = new CacheEngineImpl<>(
                cacheSize, 0, 500, true);
        var users = createUsers(putElements);
        users.forEach(user -> cache.put(user.getId(), user));

        Thread.sleep(700);
        users.forEach(user -> cache.get(user.getId()));
        assertEquals(putElements, cache.getHitCount(),
                "Incorrect number of hits for idle time check");
        Thread.sleep(700);
        users.forEach(user -> cache.get(user.getId()));
        assertEquals(0, cache.getMissCount(),
                "Incorrect number of misses for idle time check");
    }

    @Test
    void checkDuplicatesNotAddedToCache() {
        int cacheSize = 10;
        int putElements = 10;
        CacheEngine<Long, User> cache = new CacheEngineImpl<>(
                cacheSize, 0, 0, true);
        var users = createUsers(putElements);
        users.forEach(user -> cache.put(user.getId(), user));
        users.forEach(user -> cache.put(user.getId(), user));
        users.forEach(user -> cache.put(user.getId(), user));
        users.forEach(user -> cache.get(user.getId()));
        assertEquals(putElements, cache.getHitCount(),
                "Duplicate elements are added");
    }

    @Test
    void checkRemoveElement() {
        CacheEngine<Long, User> cache = new CacheEngineImpl<>(
                10, 0, 0, true);
        cache.put(1L, new User());
        assertNotNull(cache.get(1L), "no user in cache");
        cache.remove(1L);
        assertNull(cache.get(1L), "user is not removed from cache");
    }

    //todo: check the test
    @Test
    void checkNulls() {
        CacheEngine<Long, User> cache = new CacheEngineImpl<>(
                10, 0, 0, true);
        cache.put(null, new User());
        cache.put(1L, null);
        cache.put(null, null);
        cache.get(1L);
        cache.get(null);
    }

    @Test
    void checkEventListeners() {

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
