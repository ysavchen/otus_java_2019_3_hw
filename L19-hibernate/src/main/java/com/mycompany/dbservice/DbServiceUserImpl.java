package com.mycompany.dbservice;

import com.mycompany.dao.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class DbServiceUserImpl implements DbServiceUser {

    private final SessionFactory sessionFactory;

    public DbServiceUserImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public long saveUser(User user) {
        long id;
        try (Session session = sessionFactory.openSession()) {
            System.out.println("Save user");
            session.beginTransaction();
            session.saveOrUpdate(user);
            id = user.getId();
            session.getTransaction().commit();
        }
        return id;
    }

    @Override
    public Optional<User> getUser(long id) {
        System.out.println("Get user by id = " + id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);

            if (user != null) {
                Hibernate.initialize(user.getAddressData());
                Hibernate.initialize(user.getPhoneData());
            }

            session.getTransaction().commit();
            return Optional.ofNullable(user);
        }
    }
}
