package com.fyr.activiti.learning.repository;

import com.fyr.activiti.learning.entity.ConfigurationEntity;
import com.fyr.activiti.learning.entity.FileEntity;
import org.springframework.data.jpa.mapping.JpaPersistentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.File;

@Repository
public interface FileMgmtRepository extends JpaRepository<FileEntity, Long>, JpaPersistentEntity<FileEntity> {
}
