package com.mycompany.dbservice;

import com.mycompany.dao.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.RollbackException;
import java.util.Optional;

public class DbServiceUserImpl implements DbServiceUser {

    private final SessionFactory sessionFactory;

    public DbServiceUserImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public long saveUser(User user) {
        System.out.println("Save user");
        long id = 0;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                session.saveOrUpdate(user);
                id = user.getId();
                session.getTransaction().commit();
            } catch (RollbackException ex) {
                session.getTransaction().rollback();
                System.out.println("Save is not successful" + ex.toString());
            }
        }
        return id;
    }

    @Override
    public Optional<User> getUser(long id) {
        System.out.println("Get user by id = " + id);
        User user = null;
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

            return Optional.ofNullable(user);
        }
    }
}
