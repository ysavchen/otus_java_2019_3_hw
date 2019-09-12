package com.mycompany.jetty_server;

import com.mycompany.jetty_server.dbservice.DbServiceUserImpl;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DbUtils {

    public static DbServiceUserImpl connectToDb() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        final SessionFactory sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();

        return new DbServiceUserImpl(sessionFactory);
    }
}
