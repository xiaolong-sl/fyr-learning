package com.fyr.activiti.learning.security;

import org.springframework.security.authentication.AccountStatusException;

public class ConflictAccountException extends AccountStatusException {
    public ConflictAccountException(String msg) {
        super(msg);
    }
}
