package org.example.service.config;

import org.example.service.util.ConfigurationUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import java.lang.reflect.Proxy;

@Configuration
@ComponentScan(basePackages = "org.example")
public class LibraryConfiguration {

    @Bean
    public static SessionFactory buildSessionFactory() {
        org.hibernate.cfg.Configuration configuration = ConfigurationUtil.buildConfiguration();
        configuration.configure();
        return configuration.buildSessionFactory();
    }

    @Bean
    public EntityManager entityManager() {
        var sessionFactory = ConfigurationUtil.buildSessionFactory();
        return (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
    }
}
