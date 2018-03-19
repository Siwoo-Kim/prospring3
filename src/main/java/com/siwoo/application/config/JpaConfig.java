package com.siwoo.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Jpa Configuration context.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(value = "com.siwoo.application",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = Configuration.class))
public class JpaConfig {

    /**
     * Data source data source.
     *
     * @return the data source
     */
    @Bean
    public DataSource dataSource(){
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                /*.addScript("classpath:/META-INF/db/schema.sql")*/
                .build();
    }

    /**
     * Transaction manager platform transaction manager.
     *
     * @return the platform transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager(){
        return new JpaTransactionManager(entityManagerFactory());
    }

    /**
     * Entity manager factory entity manager factory.
     *
     * @return the entity manager factory
     */
    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("com.siwoo.application.domain");
        factoryBean.setJpaProperties(jpaProperties());
        /*hibernate jpa adapter*/
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.afterPropertiesSet();
        return factoryBean.getNativeEntityManagerFactory();
    }
    /**
     * Jpa Properties.
     *
     * @return the jpa Properties
     */
    private Properties jpaProperties() {
        Properties hibernateProp = new Properties();
        hibernateProp.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        hibernateProp.put("hibernate.hbm2ddl.auto", "create");
        hibernateProp.put("hibernate.format_sql", true);
        hibernateProp.put("hibernate.use_sql_comments", true);
        hibernateProp.put("hibernate.show_sql", true);
        hibernateProp.put("hibernate.max_fetch_depth", 3);
        hibernateProp.put("hibernate.jdbc.batch_size", 10);
        hibernateProp.put("hibernate.jdbc.fetch_size", 50);
        return hibernateProp;
    }

}
