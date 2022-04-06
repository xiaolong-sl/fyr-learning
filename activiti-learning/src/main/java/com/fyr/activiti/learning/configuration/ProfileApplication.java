package com.fyr.activiti.learning.configuration;

import com.google.common.cache.*;
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
import java.util.concurrent.TimeUnit;

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

    @Bean
    public LoadingCache<String, Object> loadingCache() {
        return (LoadingCache<String, Object>) CacheBuilder.newBuilder()
                //设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(8)
                //设置写缓存后8秒钟过期
                .expireAfterWrite(60, TimeUnit.SECONDS)
                //设置缓存容器的初始容量为10
                .initialCapacity(10)
                //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(100)
                //设置缓存的移除通知
                .removalListener(new RemovalListener<String, Object>() {
                    @Override
                    public void onRemoval(
                            RemovalNotification<String, Object> notification) {
                        // System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause());
                        LOGGER.warn("*** Guava Local Cache Element[ {} ] removed due to '{}'", notification.getKey(), notification.getCause());
                    }
                })
                //设置要统计缓存的命中率
                .recordStats()
                .build(new CacheLoader<String, Object>() {
                           @Override
                           public Object load(String key) throws Exception {
                               return null;
                           }
                       }
                );
    }

}
