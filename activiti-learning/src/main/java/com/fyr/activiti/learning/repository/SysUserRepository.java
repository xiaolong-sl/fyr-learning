package com.fyr.activiti.learning.repository;

import com.fyr.activiti.learning.entity.SysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends JpaRepository<SysUserEntity, Long> {

    public SysUserEntity findByUsername(String username);
}
