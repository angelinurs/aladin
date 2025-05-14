package com.korutil.server.config.datasource.user;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.user.datasource")
public record UserDatasourceProperties (
    String jdbcUrl,
    String username,
    String password,
    String driverClassName
) {}