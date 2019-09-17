package com.mycompany.spring_server.repository;

import com.mycompany.spring_server.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public long saveUser(User user) {
        logger.info("Save user");
        long id = 0L;

        Session session = sessionFactory.openSession();
        try (session) {
            session.beginTransaction();

            session.saveOrUpdate(user);
            id = user.getId();

            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            logger.error("Save is not successful", ex);
        }
        return id;
    }

    @Override
    public Optional<User> getUser(long id) {
        logger.info("Get user by id = " + id);
        User user = null;

        Session session = sessionFactory.openSession();
        try (session) {
            session.beginTransaction();

            user = session.get(User.class, id);

            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            logger.error("Get user (id = " + id + ") is not successful", ex);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Get all users");
        List<User> users = new ArrayList<>();

        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            users = session
                    .createNamedQuery("getAllUsers", User.class)
                    .getResultList();

            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            logger.error("Get all users is not successful", ex);
        }
        return users;
    }
}
