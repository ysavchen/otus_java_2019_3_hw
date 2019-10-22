package com.mycompany.msapp.repository;

import com.mycompany.msapp.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@Transactional
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
        try {
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
        try {
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
                    .createQuery("select user.* from User user", User.class)
                    .getResultList();

            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            logger.error("Get all users is not successful", ex);
        }
        return users;
    }
}
