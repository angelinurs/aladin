package com.korutil.server.config.datasource.user;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableEncryptableProperties
@EnableJpaRepositories(
        basePackages = "com.korutil.server.repository.jpa.user",
        entityManagerFactoryRef = "userEntityManagerFactory",
        transactionManagerRef = "userTransactionManager"
)
@EnableConfigurationProperties(UserDatasourceProperties.class)
public class UserDatasourceConfig {

    @Primary
    @Bean(name = "userDataSource")
    public DataSource userDataSource(UserDatasourceProperties props,
                                     @Qualifier("globalHikariConfig") HikariConfig hikariConfig) {

        // 개별 설정을 HikariConfig에 수동으로 적용
        hikariConfig.setJdbcUrl(props.jdbcUrl());
        hikariConfig.setUsername(props.username());
        hikariConfig.setPassword(props.password());
        hikariConfig.setDriverClassName(props.driverClassName());

        return new HikariDataSource(hikariConfig);
    }

    @Primary
    @Bean(name = "userEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("userDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder ) {

        return builder
                .dataSource(dataSource)
                .packages("com.korutil.server.domain")
                .persistenceUnit("user")
                .build();
    }

    @Primary
    @Bean(name = "userTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("userEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Primary
    @Bean(name = "userJdbcTemplate")
    public JdbcTemplate jdbcTemplate(
            @Qualifier("userDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
