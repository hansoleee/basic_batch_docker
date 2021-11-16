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
@EnableJpaRepositories(
        basePackages = DataSourceConfigUtils.BASE_PACKAGE + ".target.repository",
        entityManagerFactoryRef = "targetEntityManagerFactory",
        transactionManagerRef = "targetTransactionManager"
)
@EnableTransactionManagement(proxyTargetClass = true)
public class TargetDatasourceConfig {

    private final DataSourceProperties dataSourceProperties;

    public TargetDatasourceConfig(
            @Qualifier("targetDataSourceProperties") DataSourceProperties dataSourceProperties
    ) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Bean(name = "targetDataSource")
    public DataSource targetDataSource() {
        return DataSourceConfigUtils.getDataSource(
                dataSourceProperties.getDriverClassName(),
                dataSourceProperties.getUrl(),
                dataSourceProperties.getUsername(),
                dataSourceProperties.getPassword()
        );
    }

    @Bean(name = "targetEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean targetEntityManagerFactory(
            @Qualifier("targetDataSource") DataSource dataSource
    ) {
        return DataSourceConfigUtils.entityManagerFactoryBean(
                dataSource,
                ".target.domain",
                dataSourceProperties.getDialect()
        );
    }

    @Bean(name = "targetTransactionManager")
    public PlatformTransactionManager targetTransactionManager(
            @Qualifier("targetEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory
    ) {
        return DataSourceConfigUtils.jpaTransactionManager(entityManagerFactory.getObject());
    }
}
