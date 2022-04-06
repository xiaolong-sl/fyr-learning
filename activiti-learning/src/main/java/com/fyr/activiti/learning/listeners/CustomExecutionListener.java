package com.fyr.activiti.learning.listeners;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomExecutionListener implements ExecutionListener {
    public static final Logger logger = LoggerFactory.getLogger(CustomExecutionListener.class);
    private Expression expr;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        String eventName = delegateExecution.getEventName();
        String flowName = delegateExecution.getCurrentFlowElement().getName();

        logger.info("****** [ {} ] ******", expr.getValue(delegateExecution));
        switch (eventName) {
            case "start":
                logger.info("*** ready to start event: {}", flowName);
                break;
            case "end":
                logger.info("*** ready to end event: {}", flowName);
                break;
            case "take":
                logger.info("*** ready to take event: {}", flowName);
                break;
            default:
                logger.info("");
                break;
        }

    }

}
