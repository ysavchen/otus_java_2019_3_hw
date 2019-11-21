package com.mycompany.mutiprocess.database.service;

import com.mycompany.mutiprocess.database.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
public class DBServiceImpl implements DBService {

    private final SessionFactory sessionFactory;

    public DBServiceImpl(SessionFactory sessionFactory) {
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
    public List<User> getAllUsers() {
        logger.info("Get all users");
        List<User> users = new ArrayList<>();

        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            users = session
                    .createQuery("from User", User.class)
                    .getResultList();

            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            logger.error("Get all users is not successful", ex);
        }
        return users;
    }
}
