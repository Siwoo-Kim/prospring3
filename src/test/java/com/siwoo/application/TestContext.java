package com.siwoo.application;

import com.siwoo.application.config.JpaConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import static org.junit.Assert.*;

/**
 * Testing jpa-context.xml Context
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/config/jpa-context.xml")
public class TestContext {

    /**
     * The Entity manager factory.
     */
    @PersistenceUnit
    EntityManagerFactory entityManagerFactory;

    /**
     * Testing context
     */
    @Test
    public void testContext(){
        assertNotNull(entityManagerFactory);
    }


    /**
     * Testing annotation context
     */
    @Test @DirtiesContext
    public void testAnnoContext(){
        ApplicationContext c = new AnnotationConfigApplicationContext(JpaConfig.class);
        entityManagerFactory = c.getBean(EntityManagerFactory.class);
        testContext();
    }

}
