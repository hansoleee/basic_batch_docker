package com.hansoleee.basic_batch_docker.config.database;

import com.hansoleee.basic_batch_docker.config.database.property.DataSourceProperties;
import com.hansoleee.basic_batch_docker.util.DataSourceConfigUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = DataSourceConfigUtils.BASE_PACKAGE + ".batch.repository")
@EnableTransactionManagement(proxyTargetClass = true)
public class BatchDataSourceConfig {

    private final DataSourceProperties dataSourceProperties;

    public BatchDataSourceConfig(
            @Qualifier("batchDataSourceProperties") DataSourceProperties dataSourceProperties
    ) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Primary
    @Bean(name = "batchDataSource")
    public DataSource batchDataSource() {
        return DataSourceConfigUtils.getDataSource(
                dataSourceProperties.getDriverClassName(),
                dataSourceProperties.getUrl(),
                dataSourceProperties.getUsername(),
                dataSourceProperties.getPassword()
        );
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("batchDataSource") DataSource dataSource
    ) {
        return DataSourceConfigUtils.entityManagerFactoryBean(
                dataSource,
                ".batch.domain",
                dataSourceProperties.getDialect()
                );
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory
    ) {
        return DataSourceConfigUtils.jpaTransactionManager(entityManagerFactory.getObject());
    }
}
