package ca.mcgill.ecse321;

import com.fasterxml.classmate.AnnotationConfiguration;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration.AnnotationConfig;

import ca.mcgill.ecse321.model.*;

public class SessionFactoryRule implements MethodRule {

    private SessionFactory sessionFactory;
    private Transaction transaction;
    private Session session;

    @Override
    public Statement apply(final Statement statement, FrameworkMethod method, Object test) {
        return new Statement() {

            @Override 
            public void evaluate() throws Throwable {
                sessionFactory = createSessionFactory();
                createSession();

                try {
                    statement.evaluate();
                } finally {
                    shutdown();
                }
            }
        };
    }

    private void shutdown() {
        try {
            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            sessionFactory.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SessionFactory createSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Admin.class)
                        .addAnnotatedClass(Car.class)
                        .addAnnotatedClass(Driver.class)
                        .addAnnotatedClass(Leg.class)
                        .addAnnotatedClass(Passenger.class)
                        .addAnnotatedClass(PassengerTrip.class)
                        .addAnnotatedClass(Trip.class)
                        .addAnnotatedClass(User.class); 
        
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");

        configuration.setProperty("hibernate.connection.url", "jdbc:h2:~/test");

        configuration.setProperty("hibernate.hbm2ddl.auto", "create");

        sessionFactory = configuration.buildSessionFactory();

        return sessionFactory;
    }

    public Session createSession() {
        session = sessionFactory.openSession();
        return session;
    }

    public void commit() {
        transaction.commit();
    }

    public void beginTransaction() {
        transaction = session.beginTransaction();
    }

    public Session getSession() {
        return session;
    }
}