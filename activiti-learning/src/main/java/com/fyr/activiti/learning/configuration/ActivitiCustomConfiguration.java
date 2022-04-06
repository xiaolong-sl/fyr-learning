package com.fyr.activiti.learning.configuration;

import com.fyr.activiti.learning.security.activiti.SecurityUtil;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
// @ActivitiSwitch
public class ActivitiCustomConfiguration {
    private PlatformTransactionManager transactionManager;

    @Bean
    @ConfigurationProperties(prefix = "spring.activiti.datasource")
    public DataSource activitiDataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }

    @Autowired
    public ActivitiCustomConfiguration(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Primary
    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration() throws IOException {
        System.out.println("=======================");
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        //配置项内容设置
        configuration
                //设置数据库的类型
                .setDatabaseType("mysql")
                //使用springboot自带的数据源
                .setDataSource(activitiDataSource())
                .setDatabaseSchema("ACTIVITI")
                //设置字段更新类型
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                //
                .setAsyncExecutorActivate(false)
                //设置历史记录级别
                .setHistoryLevel(HistoryLevel.FULL)
        ;

        configuration.setDeploymentMode("never-fail");
        // configuration.setDeploymentResources(getBpmnFiles());
        configuration.setTransactionManager(transactionManager);
        return configuration;
    }

    private Resource[] getBpmnFiles() throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        return resourcePatternResolver.getResources("classpath*:/processes/**/*.bpmn*");
    }
}
