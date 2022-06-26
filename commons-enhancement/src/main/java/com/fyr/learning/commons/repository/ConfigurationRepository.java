package com.fyr.learning.commons.repository;

import com.fyr.learning.commons.entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Integer> {
}
