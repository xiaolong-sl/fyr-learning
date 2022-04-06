package com.fyr.activiti.learning.security.activiti;

import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.activiti.core.common.spring.identity.ExtendedInMemoryUserDetailsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
public class CustomActivitiGroupManagerImpl implements UserGroupManager {
    private Logger logger = LoggerFactory.getLogger(ActSecurityInitializedConfigurer.class);
    private final UserDetailsService userDetailsService;

    public CustomActivitiGroupManagerImpl(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public List<String> getUserGroups(String username) {
        return (List) this.userDetailsService.loadUserByUsername(username).getAuthorities().stream().filter((a) -> {
            return a.getAuthority().startsWith("GROUP_");
        }).map((a) -> {
            return a.getAuthority().substring(6);
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> getUserRoles(String username) {
        return (List) this.userDetailsService.loadUserByUsername(username).getAuthorities().stream().filter((a) -> {
            return a.getAuthority().startsWith("ROLE_");
        }).map((a) -> {
            return a.getAuthority().substring(5);
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> getGroups() {
        return ((ExtendedInMemoryUserDetailsManager) this.userDetailsService).getGroups();
    }

    @Override
    public List<String> getUsers() {
        return ((ExtendedInMemoryUserDetailsManager) this.userDetailsService).getUsers();
    }
}
