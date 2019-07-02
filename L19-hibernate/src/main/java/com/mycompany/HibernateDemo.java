package com.mycompany;

import com.mycompany.dao.Account;
import com.mycompany.dao.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateDemo {

    private final SessionFactory sessionFactory;

    public static void main(String[] args) {
        HibernateDemo demo = new HibernateDemo();

        demo.entityExample();
    }

    private HibernateDemo() {
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml");

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Account.class)
                .getMetadataBuilder()
                .build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    private void entityExample() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = new User();
            user.setName("Michael").setAge(25);

            session.save(user);
            System.out.println(">>>>>>>>>>> created:" + user);

            System.out.println(">>>>>>>>>>> before commit");
            session.getTransaction().commit(); // В этот момент делается insert

            //  session.detach(person); //Вариант с давно существующим объектом

            // А тут select не выполняется, Person берется из кэша L1
            User selected = session.get(User.class, user.getId());
            System.out.println(">>>>>>>>>>> selected:" + selected);
        }
    }
}
