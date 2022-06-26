package com.fyr.learning.client.https.controller;

import com.fyr.learning.commons.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/client")
public class RemoteController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/hello")
    public void callServer() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://localhost:8806/https-server/test", HttpMethod.GET, null, String.class);
        System.out.println(JsonUtil.obj2StringPretty(responseEntity));
    }
}
