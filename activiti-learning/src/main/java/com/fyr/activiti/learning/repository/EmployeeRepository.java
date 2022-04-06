package com.fyr.activiti.learning.repository;

import com.fyr.activiti.learning.entity.EmployeeEntity;
import org.springframework.data.jpa.mapping.JpaPersistentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
    // EmployeeEntity findByUsername(String username);
}
