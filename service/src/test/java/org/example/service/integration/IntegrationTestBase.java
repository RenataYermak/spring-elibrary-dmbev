package org.example.service.integration;

import org.example.service.config.LibraryConfiguration;
import org.example.service.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManager;

public abstract class IntegrationTestBase {

    protected static AnnotationConfigApplicationContext context;
    protected static Session session;

    @BeforeAll
    static void init() {
        context = new AnnotationConfigApplicationContext(LibraryConfiguration.class);
        SessionFactory sessionFactory = context.getBean(SessionFactory.class);
        session = (Session) context.getBean(EntityManager.class);
        TestDataImporter.importData(sessionFactory);
    }

    @BeforeEach
    void getSession() {
        session.beginTransaction();
    }

    @AfterEach
    void closeSession() {
        session.getTransaction().rollback();
    }

    @AfterAll
    static void close() {
        context.close();
    }
}
