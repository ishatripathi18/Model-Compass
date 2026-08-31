package com.modelcompass.repository;

import com.modelcompass.entity.AIModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIModelRepository extends JpaRepository<AIModel, Long> {
}