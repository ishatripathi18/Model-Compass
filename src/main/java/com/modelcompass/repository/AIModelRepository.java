package com.modelcompass.repository;

import com.modelcompass.entity.AIModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIModelRepository extends JpaRepository<AIModel, Long> {
    List<AIModel> findByProviderIgnoreCase(String provider);
    List<AIModel> findByCategoryIgnoreCase(String category);
    List<AIModel> findByProviderIgnoreCaseAndCategoryIgnoreCase(String provider, String category);
    List<AIModel> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}