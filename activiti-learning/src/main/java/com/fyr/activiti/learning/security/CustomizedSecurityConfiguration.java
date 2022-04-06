package com.fyr.activiti.learning.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class CustomizedSecurityConfiguration {
    private Logger LOGGER = LoggerFactory.getLogger(CustomizedSecurityConfiguration.class);
    private CustomAuthenticationSuccessHandler successHandler;
    private CustomAuthenticationFailureHandler failureHandler;
    private final CustomAuthenticationProvider authenticationProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final CustomAuthenticationFilter customAuthenticationFilter;

    @Autowired
    public CustomizedSecurityConfiguration(CustomAuthenticationProvider authenticationProvider,
                                           AuthenticationEntryPoint authenticationEntryPoint,
                                           CustomAuthenticationSuccessHandler successHandler,
                                           CustomAuthenticationFailureHandler failureHandler,
                                           CustomAuthenticationFilter customAuthenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.customAuthenticationFilter = customAuthenticationFilter;
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .logout().logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        LOGGER.info("*** logout successfully");
                    }
                })
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        LOGGER.info("*** access denied");
                    }
                })
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // .maximumSessions(1)
                // .maxSessionsPreventsLogin(false)
                // .expiredSessionStrategy(new SessionInformationExpiredStrategy() {
                //     @Override
                //     public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
                //
                //     }
                // })

                // 允许所有人访问 /login/page
                .authorizeRequests().antMatchers("/login/page","/activiti/deploy").permitAll()
                .and()
                // 任意访问请求都必须先通过认证
                .authorizeRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                // 启用 iframe 功能
                .headers().frameOptions().disable()
                .and()
                // 将自定义的 AbstractAuthenticationProcessingFilter 加在 Spring 过滤器链中
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(withDefaults())
                .authenticationManager(new CustomAuthenticationManager(authenticationProvider))
                .csrf().disable()
                .cors().disable()
                .build();
    }
}
