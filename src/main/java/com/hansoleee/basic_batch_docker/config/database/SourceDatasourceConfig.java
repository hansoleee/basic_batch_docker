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
        basePackages = DataSourceConfigUtils.BASE_PACKAGE + ".source.repository",
        entityManagerFactoryRef = "sourceEntityManagerFactory",
        transactionManagerRef = "sourceTransactionManager"
)
@EnableTransactionManagement(proxyTargetClass = true)
public class SourceDatasourceConfig {

    private final DataSourceProperties dataSourceProperties;

    public SourceDatasourceConfig(
            @Qualifier("sourceDataSourceProperties") DataSourceProperties dataSourceProperties
    ) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Bean(name = "sourceDataSource")
    public DataSource sourceDataSource() {
        return DataSourceConfigUtils.getDataSource(
                dataSourceProperties.getDriverClassName(),
                dataSourceProperties.getUrl(),
                dataSourceProperties.getUsername(),
                dataSourceProperties.getPassword()
        );
    }

    @Bean(name = "sourceEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sourceEntityManagerFactory(
            @Qualifier("sourceDataSource") DataSource dataSource
    ) {
        return DataSourceConfigUtils.entityManagerFactoryBean(
                dataSource,
                ".source.domain",
                dataSourceProperties.getDialect()
        );
    }

    @Bean(name = "sourceTransactionManager")
    public PlatformTransactionManager sourceTransactionManager(
            @Qualifier("sourceEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory
    ) {
        return DataSourceConfigUtils.jpaTransactionManager(entityManagerFactory.getObject());
    }
}
