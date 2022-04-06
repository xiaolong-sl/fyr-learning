package com.fyr.activiti.learning.configuration;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Documented
@Conditional(ActivitiSwitchImpl.class)
public @interface ActivitiSwitch {
}
