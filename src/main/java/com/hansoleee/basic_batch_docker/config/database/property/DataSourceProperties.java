package com.hansoleee.basic_batch_docker.config.database.property;

public interface DataSourceProperties {

    String getDriverClassName();
    String getUrl();
    String getUsername();
    String getPassword();
    String getDialect();
}
