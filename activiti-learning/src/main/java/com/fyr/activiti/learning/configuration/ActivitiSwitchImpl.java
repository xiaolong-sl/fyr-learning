package com.fyr.activiti.learning.configuration;

import com.google.common.base.Strings;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ActivitiSwitchImpl implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String propertyValue = context.getEnvironment().getProperty("spring.activiti.enabled");

        return !Strings.isNullOrEmpty(propertyValue) && propertyValue.equalsIgnoreCase(Boolean.TRUE.toString());
    }
}
