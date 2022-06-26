package com.fyr.activiti.learning.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Component
// @ConfigurationProperties(prefix = "spring.security.csrf")
public class ProfileApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileApplication.class);

    @Value("${excluded:null}")
    private String excluded;

    @Primary
    @Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
