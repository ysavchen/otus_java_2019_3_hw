package com.mycompany;

import com.mycompany.cache.CacheEngine;
import com.mycompany.cache.CacheEngineImpl;
import com.mycompany.data.User;
import com.mycompany.dbservice.DbServiceUser;
import com.mycompany.dbservice.DbServiceUserImpl;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AppWithCacheTests {

    private static SessionFactory sessionFactory;

    @BeforeAll
    static void createConnection() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    @Test
    void checkUserCached() {
        CacheEngine<Long, User> cache = new CacheEngineImpl.Builder(10)
                .isEternal(true)
                .build();

        DbServiceUser dbService = new DbServiceUserImpl(sessionFactory);
        AppWithCache appWithCache = new AppWithCacheImpl(dbService, cache);

        User user = new User();
        user.setName("Michael").setAge(35);

        long id = appWithCache.addUser(user);
        assertEquals(user, cache.get(id),
                "User is not cached");
        assertEquals(user, dbService.getUser(id).orElse(null),
                "User is not saved or not loaded");
    }

    @Test
    void checkUserReturnedFromCache() {
        CacheEngine<Long, User> cache = new CacheEngineImpl.Builder(10)
                .isEternal(true)
                .build();

        DbServiceUser dbService = Mockito.mock(DbServiceUser.class);
        AppWithCache appWithCache = new AppWithCacheImpl(dbService, cache);

        User user = new User();
        user.setName("Michael").setAge(35);

        when(dbService.saveUser(user)).thenReturn(1L);
        long id = appWithCache.addUser(user);
        assertEquals(user, appWithCache.getUser(id).orElse(null),
                "User is not cached");
        verify(dbService, times(0)).getUser(id);
    }
}
