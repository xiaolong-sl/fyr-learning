package com.fyr.activiti.learning.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @PreAuthorize("@myAccessDecisionService.hasPermission('hello:sayHello')")
    @GetMapping("/sayHello")
    public String sayHello() {
        return "hello";
    }

//    @PreAuthorize("@myAccessDecisionService.hasPermission('hello:sayHi')")
    @GetMapping("/sayHi")
    public String sayHi() {
        return "hi";
    }
}
