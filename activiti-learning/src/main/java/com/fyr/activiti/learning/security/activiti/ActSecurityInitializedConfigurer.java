package com.fyr.activiti.learning.security.activiti;

import com.fyr.activiti.learning.security.CustomizedUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ActSecurityInitializedConfigurer extends WebSecurityConfigurerAdapter {
    private Logger logger = LoggerFactory.getLogger(ActSecurityInitializedConfigurer.class);
    private CustomizedUserDetailsService userDetailsService;

    @Autowired
    public ActSecurityInitializedConfigurer(CustomizedUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

}
