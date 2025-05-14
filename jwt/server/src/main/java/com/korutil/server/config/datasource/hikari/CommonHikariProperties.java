package com.korutil.server.config.datasource.hikari;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonHikariProperties {

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariConfig globalHikariConfig() {
        return new HikariConfig();  // 공통 Hikari 설정 등록
    }
}