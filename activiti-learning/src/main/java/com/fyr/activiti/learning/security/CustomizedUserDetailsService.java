package com.fyr.activiti.learning.security;

import com.fyr.activiti.learning.entity.SysMenuEntity;
import com.fyr.activiti.learning.entity.SysRoleEntity;
import com.fyr.activiti.learning.entity.SysUserEntity;
import com.fyr.activiti.learning.repository.SysUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomizedUserDetailsService implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(CustomizedUserDetailsService.class);
    private SysUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserEntity sysUserEntity = userRepository.findByUsername(username);
        Set<SysRoleEntity> userRoles = sysUserEntity.getRoles();
        Set<SysRoleEntity> deptRoles = sysUserEntity.getDept().getRoles();
        Set<SysRoleEntity> roleSet = new HashSet<>();
        roleSet.addAll(userRoles);
        roleSet.addAll(deptRoles);

        Set<SimpleGrantedAuthority> authorities = roleSet.stream().flatMap(role -> role.getMenus().stream())
                .filter(menu -> StringUtils.hasLength(menu.getCode()))
                .map(SysMenuEntity::getCode)
//                .map(e -> "ROLE_" + e.getCode())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new CustomizedUserDetails(sysUserEntity.getUsername(), sysUserEntity.getPassword(), 1 == sysUserEntity.getEnabled(), authorities);
    }
}
