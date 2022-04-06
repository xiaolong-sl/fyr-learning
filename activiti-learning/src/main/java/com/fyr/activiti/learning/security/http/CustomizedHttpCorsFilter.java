package com.fyr.activiti.learning.security.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// @Component
public class CustomizedHttpCorsFilter extends AbstractHttpConfigurer<CustomizedHttpCorsFilter, HttpSecurity> {
    private Logger logger = LoggerFactory.getLogger(CustomizedHttpCorsFilter.class);

    @Override
    public void init(HttpSecurity httpSecurity) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("customized csrf filter initializing ...");
        }
    }

    public CorsFilter corsFilter(CorsEndpointProperties corsConfiguration) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin(corsConfiguration.getAllowedOrigins().toString());
        config.addAllowedOriginPattern(corsConfiguration.getAllowedOriginPatterns().toString());
        // config.addAllowedMethod("GET, POST, PUT, PATCH, DELETE");
        config.addAllowedMethod(corsConfiguration.getAllowedMethods().toString());
        config.addAllowedHeader(corsConfiguration.getAllowedHeaders().toString());
        config.addExposedHeader(corsConfiguration.getExposedHeaders().toString());
        config.setAllowCredentials(corsConfiguration.getAllowCredentials());
        config.setMaxAge(corsConfiguration.getMaxAge());
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(corsFilter(new CorsEndpointProperties()), UsernamePasswordAuthenticationFilter.class);
    }

    public static CustomizedHttpCorsFilter getInstance() {
        return new CustomizedHttpCorsFilter();
    }
}
