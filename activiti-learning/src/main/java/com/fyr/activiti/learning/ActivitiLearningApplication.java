package com.fyr.activiti.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@EnableTransactionManagement
@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
        // ManagementWebSecurityAutoConfiguration.class,
        // org.activiti.core.common.spring.security.config.ActivitiSpringSecurityAutoConfiguration.class,

        // org.activiti.spring.boot.EndpointAutoConfiguration.class,
        // org.activiti.spring.boot.JpaProcessEngineAutoConfiguration.class,
        // org.activiti.spring.boot.DataSourceProcessEngineAutoConfiguration.class,
        // org.activiti.spring.boot.RestApiAutoConfiguration.class
})
public class ActivitiLearningApplication {

    public static void main(String[] args) {
        disableAccessWarnings();
        SpringApplication.run(ActivitiLearningApplication.class, args);
    }

    /**
     * 忽略非法反射警告 适用于jdk11
     */
    @SuppressWarnings("unchecked")
    public static void disableAccessWarnings() {
        try {
            Class unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Object unsafe = field.get(null);

            Method putObjectVolatile =
                    unsafeClass.getDeclaredMethod("putObjectVolatile", Object.class, long.class, Object.class);
            Method staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset", Field.class);

            Class loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field loggerField = loggerClass.getDeclaredField("logger");
            Long offset = (Long) staticFieldOffset.invoke(unsafe, loggerField);
            putObjectVolatile.invoke(unsafe, loggerClass, offset, null);
        } catch (Exception ignored) {
        }
    }

}
