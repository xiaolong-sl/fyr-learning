package com.fyr.activiti.learning.repository;

import com.fyr.activiti.learning.entity.SysUserEntity;
import org.springframework.data.jpa.mapping.JpaPersistentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends JpaRepository<SysUserEntity, Long> {

    public SysUserEntity findByUsername(String username);
}
