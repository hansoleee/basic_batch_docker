package com.hansoleee.basic_batch_docker.config.database.property.impl;

import com.hansoleee.basic_batch_docker.config.database.property.DataSourceProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("exampleDataSourceProperties")
@ConfigurationProperties(prefix = "example-datasource")
@Getter
@Setter
@ToString
public class ExampleDataSourceProperties implements DataSourceProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String dialect;
}