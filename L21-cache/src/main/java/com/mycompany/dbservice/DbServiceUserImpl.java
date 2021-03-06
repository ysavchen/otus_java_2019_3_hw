package com.mycompany.dbservice;

import com.mycompany.cache.CacheEngine;
import com.mycompany.data.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class DbServiceUserImpl implements DbServiceUser {

    private final SessionFactory sessionFactory;

    private CacheEngine<Long, User> cache;

    public DbServiceUserImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addCache(CacheEngine<Long, User> cache) {
        this.cache = cache;
    }

    public void removeCache() {
        cache.dispose();
        cache = null;
    }

    @Override
    public long saveUser(User user) {
        System.out.println("Save user in DB");
        long id = 0;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                session.saveOrUpdate(user);
                id = user.getId();
                session.getTransaction().commit();
            } catch (Exception ex) {
                session.getTransaction().rollback();
                System.out.println("Save is not successful" + ex.toString());
            }
        }
        putUserToCache(user);
        return id;
    }

    @Override
    public Optional<User> getUser(long id) {
        System.out.println("Get user by id = " + id + " from DB");
        User user = getUserFromCache(id);
        if (user == null) {
            try (Session session = sessionFactory.openSession()) {
                try {
                    session.beginTransaction();
                    user = session.get(User.class, id);

                    if (user != null) {
                        Hibernate.initialize(user.getAddressData());
                        Hibernate.initialize(user.getPhoneData());
                    }

                    session.getTransaction().commit();
                } catch (Exception ex) {
                    session.getTransaction().rollback();
                    System.out.println("Save is not successful" + ex.toString());
                }
            }
        }

        putUserToCache(user);
        return Optional.ofNullable(user);
    }

    private User getUserFromCache(long id) {
        if (cache != null) {
            return cache.get(id);
        }
        return null;
    }

    private void putUserToCache(User user) {
        if (cache != null && user != null && user.getId() != 0) {
            cache.put(user.getId(), user);
        }
    }
}
