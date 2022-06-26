package com.fyr.learning.file.repository;

import com.fyr.learning.file.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
    // EmployeeEntity findByUsername(String username);
}
