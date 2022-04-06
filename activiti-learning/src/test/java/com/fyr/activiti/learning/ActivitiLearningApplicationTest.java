package com.fyr.activiti.learning;

import com.fyr.activiti.learning.service.ExcelTest;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit test for simple App.
 */
// @RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiLearningApplicationTest {
    private Logger LOGGER = LoggerFactory.getLogger(ActivitiLearningApplicationTest.class);

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Autowired
    ExcelTest test;

    @Test
    public void queryProcess() {
        List<ProcessDefinition> processDefinitionList = processEngine.getRepositoryService().createProcessDefinitionQuery().deploymentId("1").orderByProcessDefinitionVersion().asc().list();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            System.out.println("流程定义ID:" + processDefinition.getId());
            System.out.println("流程定义的名称:" + processDefinition.getName());
            System.out.println("流程定义的key:" + processDefinition.getKey());
            System.out.println("流程定义的版本:" + processDefinition.getVersion());
            System.out.println("资源名称bpmn文件:" + processDefinition.getResourceName());
            System.out.println("资源名称png文件:" + processDefinition.getDiagramResourceName());
            System.out.println("部署对象ID:" + processDefinition.getDeploymentId());
            System.out.println("---------------------------------------------------");
        }
    }

    @Test
    public void countProcess() {
        long count = processEngine.getRepositoryService().createProcessDefinitionQuery().count();
        LOGGER.info("Number of process definitions: {}", count);
    }

    @Test
    public void startProcess() {
        Authentication.setAuthenticatedUserId("System");
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employeeName", "Kermit");
        variables.put("numberOfDays", new Integer(4));
        variables.put("vacationMotivation", "I'm really tired!");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacationRequest", variables);

        // Verify that we started a new process instance
        LOGGER.info("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());
    }

    @Test
    public void managerApprove() {
        // Fetch all tasks for the management group
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        for (Task task : tasks) {
            LOGGER.info("Task available: " + task.getName());
        }

        Task task = tasks.get(0);

        Map<String, Object> taskVariables = new HashMap<String, Object>();
        taskVariables.put("vacationApproved", "true");
        taskVariables.put("managerMotivation", "We have a tight deadline!");
        taskService.complete(task.getId(), taskVariables);
    }

    @Test
    public void adjustRequest() {
        // Fetch all tasks for the management group
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("Kermit").list();
        for (Task task : tasks) {
            LOGGER.info("Task available: " + task.getName());
        }

        Task task = tasks.get(0);

        Map<String, Object> taskVariables = new HashMap<String, Object>();
        taskVariables.put("resendRequest", "true");
        taskVariables.put("numberOfDays", Integer.valueOf(2));
        // taskVariables.put("managerMotivation", "We have a tight deadline!");
        taskService.complete(task.getId(), taskVariables);

    }

    @Test
    public void generateDiagram() {
        InputStream inputStream = null;
        RuntimeService runtimeService = processEngine.getRuntimeService();

        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().list();
        ProcessInstance processInstance = processInstances.stream().filter(x -> x.getProcessInstanceId().equals("5001")).findFirst().get();
        if (!ObjectUtils.isEmpty(processInstance)) {
            BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(processInstance.getProcessDefinitionId());
            // if (!ObjectUtils.isEmpty(bpmnModel) && bpmnModel.getLocationMap().size() > 0) {
            // inputStream = new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, runtimeService.getActiveActivityIds(processInstance.getProcessDefinitionId()));
            inputStream = new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, "WenQuanYi Micro Hei", "WenQuanYi Micro Hei", null);

            // }
        }
        try {
            StreamUtils.copy(inputStream, new FileOutputStream(new File("src/main/resources/processes/image" + Instant.now().getEpochSecond() + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void startMyProcess() {
        Authentication.setAuthenticatedUserId("System");
        // Map<String, Object> variables = new HashMap<String, Object>();
        // variables.put("employeeName", "Kermit");
        // variables.put("numberOfDays", new Integer(4));
        // variables.put("vacationMotivation", "I'm really tired!");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess");

        // Verify that we started a new process instance
        LOGGER.info("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());
    }

    @Test
    void exportExcel() {
        test.generateExcelFile();
    }
}
