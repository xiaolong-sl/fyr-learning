package com.fyr.learning.client.https.application;

import com.fyr.learning.client.https.HttpsClientSpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HttpsClientSpringBootApplication.class);
    }

}
