package com.fyr.learning.server.https.dto;

import java.util.Set;

public class User {
    private String username;

    private String password;

    private Boolean enabled;

    private Boolean expired;

    private Boolean locked;

    private Set<String> roles;

    public User() {
    }

    public User(String username, String password, Boolean enabled, Boolean expired, Boolean locked) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.expired = expired;
        this.locked = locked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" + "username='" + username + '\'' + ", password='" + password + '\'' + ", enabled=" + enabled +
                ", expired=" + expired + ", locked=" + locked + ", roles=" + roles + '}';
    }
}
