package com.hansoleee.basic_batch_docker.util;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

public class DataSourceConfigUtils {

    public static final String BASE_PACKAGE = "com.hansoleee.basic_batch_docker";

    public static DataSource getDataSource(
            String driverClassName,
            String url,
            String username,
            String password
    ) {
        final HikariDataSource dataSource = new HikariDataSource();

        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    public static LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
            DataSource dataSource,
            String packagesToScan,
            String dialect
    ) {
        final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();

        emf.setDataSource(dataSource);
        emf.setJpaVendorAdapter(jpaVendorAdapter(dialect));
        emf.setPackagesToScan(new String[]{BASE_PACKAGE + packagesToScan});
        emf.setJpaProperties(jpaProperties());

        return emf;
    }

    public static JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();

        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        jpaTransactionManager.setJpaDialect(new HibernateJpaDialect());

        return jpaTransactionManager;
    }


    private static Properties jpaProperties() {
        final Properties properties = new Properties();

        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.format_sql", "false");
        properties.setProperty("hibernate.use_sql_comments", "false");
        properties.setProperty("hibernate.globally_quoted_identifiers", "true");

        properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");

        properties.setProperty("hibernate.jdbc.batch_size", "1000");
        properties.setProperty("hibernate.order_inserts", "true");
        properties.setProperty("hibernate.order_updates", "true");
        properties.setProperty("hibernate.jdbc.batch_versioned_data", "true");

        properties.setProperty("spring.jpa.hibernate.jdbc.batch_size", "1000");
        properties.setProperty("spring.jpa.hibernate.order_inserts", "true");
        properties.setProperty("spring.jpa.hibernate.order_updates", "true");

        properties.setProperty("spring.jpa.hibernate.jdbc.batch_versioned_data", "true");
        properties.setProperty("spring.jpa.properties.hibernate.jdbc.batch_size", "1000");
        properties.setProperty("spring.jpa.properties.hibernate.order_inserts", "true");
        properties.setProperty("spring.jpa.properties.hibernate.order_updates", "true");
        properties.setProperty("spring.jpa.properties.hibernate.jdbc.batch_versioned_data", "true");

        return properties;
    }

    private static JpaVendorAdapter jpaVendorAdapter(String dialect) {
        final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        jpaVendorAdapter.setDatabasePlatform(dialect);

        return jpaVendorAdapter;
    }
}
