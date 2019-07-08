package com.mycompany.dbservice;

import com.mycompany.dao.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class DbServiceUserImpl implements DbServiceUser {

    private final SessionFactory sessionFactory;

    public DbServiceUserImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public Optional<User> getUser(long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.getTransaction().commit();
            return Optional.ofNullable(user);
        }
    }
}
