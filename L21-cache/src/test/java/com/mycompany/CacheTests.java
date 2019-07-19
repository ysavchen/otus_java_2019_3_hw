package com.mycompany;

import com.mycompany.cache.CacheEngine;
import com.mycompany.cache.CacheEngineImpl;
import com.mycompany.cache.CacheListener;
import com.mycompany.cache.EventType;
import com.mycompany.data.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CacheTests {

    @Test
    void checkHitCount() {
        int cacheSize = 10;
        int putElements = 10;
        CacheEngine<Long, User> cache = new CacheEngineImpl.Builder(cacheSize)
                .isEternal(true)
                .build();

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
        CacheEngine<Long, User> cache = new CacheEngineImpl.Builder(cacheSize)
                .isEternal(true)
                .build();

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
        CacheEngine<Long, User> cache = new CacheEngineImpl.Builder(cacheSize)
                .withLifeTime(500)
                .build();

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
        CacheEngine<Long, User> cache = new CacheEngineImpl.Builder(cacheSize)
                .withLifeTime(500)
                .isEternal(true)
                .build();

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
        CacheEngine<Long, User> cache = new CacheEngineImpl.Builder(cacheSize)
                .withIdleTime(500)
                .build();

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
        Thread.sleep(700);
        users.forEach(user -> cache.get(user.getId()));
        assertEquals(putElements, cache.getMissCount(),
                "Incorrect number of misses for idle time check");
    }

    @Test
    void checkIdleTimeWithEternalTrue() throws InterruptedException {
        int cacheSize = 10;
        int putElements = 10;
        CacheEngine<Long, User> cache = new CacheEngineImpl.Builder(cacheSize)
                .withIdleTime(500)
                .isEternal(true)
                .build();

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
        CacheEngine<Long, User> cache = new CacheEngineImpl.Builder(cacheSize)
                .isEternal(true)
                .build();

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
        CacheEngine<Long, User> cache = new CacheEngineImpl.Builder(10)
                .isEternal(true)
                .build();

        cache.put(1L, new User());
        assertNotNull(cache.get(1L), "no user in cache");
        cache.remove(1L);
        assertNull(cache.get(1L), "user is not removed from cache");
    }

    @Test
    void checkPutListener() {
        CacheEngine<Long, User> cache = new CacheEngineImpl.Builder(10)
                .isEternal(true)
                .build();
        User user = new User();

        CacheListener<Long, User> putListener = Mockito.mock(CacheListener.class);
        cache.addListener(putListener, EventType.PUT);
        cache.put(1L, user);
        verify(putListener, times(1)).notify(1L, user, EventType.PUT);

        cache.removeListener(putListener, EventType.PUT);
        cache.put(2L, user);
        verify(putListener, times(0)).notify(2L, user, EventType.PUT);
    }

    @Test
    void checkRemoveListener() {
        CacheEngine<Long, User> cache = new CacheEngineImpl.Builder(10)
                .isEternal(true)
                .build();
        User user = new User();

        CacheListener<Long, User> removeListener = Mockito.mock(CacheListener.class);
        cache.addListener(removeListener, EventType.REMOVE);
        cache.put(1L, user);
        cache.remove(1L);
        verify(removeListener, times(1)).notify(1L, user, EventType.REMOVE);

        cache.removeListener(removeListener, EventType.REMOVE);
        cache.put(2L, user);
        cache.remove(2L);
        verify(removeListener, times(0)).notify(2L, user, EventType.REMOVE);
    }

    @Test
    void checkNulls() {
        CacheEngine<Long, User> cache = new CacheEngineImpl.Builder(10)
                .isEternal(true)
                .build();
        CacheListener<Long, User> putListener = Mockito.mock(CacheListener.class);
        cache.addListener(putListener, EventType.PUT);

        User user = new User();
        cache.put(null, new User());
        verify(putListener, times(0)).notify(1L, user, EventType.PUT);

        cache.put(1L, null);
        verify(putListener, times(0)).notify(1L, null, EventType.PUT);

        cache.put(null, null);
        verify(putListener, times(0)).notify(null, null, EventType.PUT);

        assertNull(cache.get(null));
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
