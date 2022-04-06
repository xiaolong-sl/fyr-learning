package com.fyr.activiti.learning.security.activiti;

import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public class ActGroupEntityManager implements UserGroupManager {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public List<String> getUserGroups(String s) {
        return null;
    }

    @Override
    public List<String> getUserRoles(String s) {
        return null;
    }

    @Override
    public List<String> getGroups() {
        return null;
    }

    @Override
    public List<String> getUsers() {
        return null;
    }
}
