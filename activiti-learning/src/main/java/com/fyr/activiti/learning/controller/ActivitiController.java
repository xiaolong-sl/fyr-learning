package com.fyr.activiti.learning.controller;

import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activiti")
public class ActivitiController {

    @Autowired
    RepositoryService repositoryService;

    @GetMapping("/deploy")
    public void sayHello() {
        repositoryService.createDeployment().addClasspathResource("processes/VacationRequest.bpmn20.xml").name("Vacation Request").deploy();
    }

}
