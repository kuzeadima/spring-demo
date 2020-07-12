package com.thekuzea.experimental.config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@TestConfiguration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.thekuzea.experimental.domain.dao")
public class PersistenceConfig {

    @Bean
    public DataSource dataSource(@Value("${spring.datasource.driver-class-name}") final String driverClassName,
                                 @Value("${spring.datasource.username}") final String username,
                                 @Value("${spring.datasource.password}") final String password,
                                 @Value("${spring.datasource.url}") final String url) {

        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        return dataSource;
    }

    @Bean
    public Properties hibernateProperties(@Value("${spring.jpa.database-platform}") final String dialect,
                                          @Value("${spring.jpa.hibernate.ddl-auto}") final String ddl) {

        final Properties properties = new Properties();
        properties.setProperty("hibernate.ddl-auto", ddl);
        properties.setProperty("hibernate.dialect", dialect);
        return properties;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(final DataSource dataSource,
                                                     final Properties hibernateProperties) {

        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.thekuzea.experimental.domain.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(hibernateProperties);
        em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        em.afterPropertiesSet();
        return em.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }
}
