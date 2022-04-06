package com.fyr.activiti.learning.security;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    public CustomAuthenticationFailureHandler() {
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof UsernameNotFoundException) {
            response.sendRedirect("/login/page?inexistent");
        } else if (exception instanceof DisabledException) {
            response.sendRedirect("/login/page?disabled");
        } else if (exception instanceof AccountExpiredException) {
            response.sendRedirect("/login/page?expired");
        } else if (exception instanceof LockedException) {
            response.sendRedirect("/login/page?locked");
        } else {
            response.sendRedirect("/login/page?error");
        }
    }
}
