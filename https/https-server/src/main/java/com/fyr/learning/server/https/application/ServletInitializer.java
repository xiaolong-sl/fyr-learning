package com.fyr.learning.server.https.application;

import com.fyr.learning.server.https.HttpsServerSpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HttpsServerSpringBootApplication.class);
    }

}
