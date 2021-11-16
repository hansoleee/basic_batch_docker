package com.hansoleee.basic_batch_docker.config.database;

import com.hansoleee.basic_batch_docker.config.database.property.DataSourceProperties;
import com.hansoleee.basic_batch_docker.util.DataSourceConfigUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = DataSourceConfigUtils.BASE_PACKAGE + ".example.repository",
        entityManagerFactoryRef = "exampleEntityManagerFactory",
        transactionManagerRef = "exampleTransactionManager"
)
@EnableTransactionManagement(proxyTargetClass = true)
public class ExampleDatasourceConfig {

    private final DataSourceProperties dataSourceProperties;

    public ExampleDatasourceConfig(
            @Qualifier("exampleDataSourceProperties") DataSourceProperties dataSourceProperties
    ) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Bean(name = "exampleDataSource")
    public DataSource exampleDataSource() {
        return DataSourceConfigUtils.getDataSource(
                dataSourceProperties.getDriverClassName(),
                dataSourceProperties.getUrl(),
                dataSourceProperties.getUsername(),
                dataSourceProperties.getPassword()
        );
    }

    @Bean(name = "exampleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean exampleEntityManagerFactory(
            @Qualifier("exampleDataSource") DataSource dataSource
    ) {
        return DataSourceConfigUtils.entityManagerFactoryBean(
                dataSource,
                ".example.domain",
                dataSourceProperties.getDialect()
        );
    }

    @Bean(name = "exampleTransactionManager")
    public PlatformTransactionManager exampleTransactionManager(
            @Qualifier("exampleEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory
    ) {
        return DataSourceConfigUtils.jpaTransactionManager(entityManagerFactory.getObject());
    }
}
