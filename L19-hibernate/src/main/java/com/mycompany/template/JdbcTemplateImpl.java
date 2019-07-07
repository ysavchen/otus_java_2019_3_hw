package com.mycompany.template;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class JdbcTemplateImpl implements JdbcTemplate {

    private final SessionFactory sessionFactory;

    public JdbcTemplateImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Object objectData) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(objectData);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Object objectData) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(objectData);
            session.getTransaction().commit();
        }
    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            T entity = session.load(clazz, id);
            Hibernate.initialize(entity);
            session.getTransaction().commit();
            return entity;
        }
    }
}
