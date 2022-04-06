package com.fyr.activiti.learning.security;

import com.fyr.activiti.learning.dto.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Bean
    public List<User> preloadUsers() {
        return Arrays.asList(new User("user1", "password1", true, false, false),
                new User("user2", "password2", false, false, false),
                new User("user3", "password3", true, true, false),
                new User("user4", "password4", true, false, true));
    }

    private List<User> getUser(String username) {
        return preloadUsers().stream().filter(user -> user.getUsername().equals(username)).collect(Collectors.toList());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取用户登录时输入的用户名
        String username = authentication.getName();
        // 根据用户名查询系统中的用户信息
        List<User> users = getUser(username);
        // 如果用户列表为 null，说明查找用户功能出现异常，抛出 AuthenticationServiceException
        if (Objects.isNull(users)) {
            throw new AuthenticationServiceException(String.format("Searching user[%s] occurred error!", username));
        }
        // 如果用户列表为空，说明没有匹配的用户，抛出 UsernameNotFoundException
        if (users.size() == 0) {
            throw new UsernameNotFoundException(String.format("No qualified user[%s]!", username));
        }
        // 如果用户列表中不止一个匹配用户，说明系统中用户唯一性逻辑存在问题，抛出 ConflictAccountException
        if (users.size() > 1) {
            throw new ConflictAccountException(String.format("Conflict user[%s]", username));
        }
        // 获取用户列表中唯一的用户对象
        User user = users.get(0);
        // 如果用户没有设置启用或禁用状态，或者用户被设为禁用，则抛出 DisabledException
        Optional<Boolean> enabled = Optional.of(user.getEnabled());
        if (!enabled.orElse(false)) {
            throw new DisabledException(String.format("User[%s] is disabled!", username));
        }
        // 如果用户没有过期状态或过期状态为 true 则抛出 AccountExpiredException
        Optional<Boolean> expired = Optional.of(user.getExpired());
        if (expired.orElse(true)) {
            throw new AccountExpiredException(String.format("User[%s] is expired!", username));
        }
        // 如果用户没有锁定状态或锁定状态为 true 则抛出 LockedException
        Optional<Boolean> locked = Optional.of(user.getLocked());
        if (locked.orElse(true)) {
            throw new LockedException(String.format("User[%s] is locked!", username));
        }
        // 如果用户登录时输入的密码和系统中密码匹配，则返回一个完全填充的 Authentication 对象
        if (user.getPassword().equals(authentication.getCredentials().toString())) {
            return new UsernamePasswordAuthenticationToken(authentication, authentication.getCredentials(), new ArrayList<>());
        }
        // 如果密码不匹配则返回 null（此处可以抛异常，试具体应用场景而定）
        return null;
        // return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
