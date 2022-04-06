package com.fyr.activiti.learning.listeners;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomTaskListener implements TaskListener {
    public static final Logger logger = LoggerFactory.getLogger(CustomTaskListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        String taskEventName = delegateTask.getEventName();
        String taskName = delegateTask.getName();

        switch (taskEventName) {
            case "start":
                logger.info("*** ready to start task: {}", taskName);
                break;
            case "end":
                logger.info("*** ready to end task: {}", taskName);
                break;
            case "take":
                logger.info("*** ready to take task: {}", taskName);
                break;
            default:
                logger.info("");
                break;
        }
    }
}
