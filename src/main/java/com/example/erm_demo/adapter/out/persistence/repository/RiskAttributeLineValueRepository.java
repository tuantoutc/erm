package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.RiskAttributeLineValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskAttributeLineValueRepository extends JpaRepository<RiskAttributeLineValueEntity, Long> {

    List<RiskAttributeLineValueEntity> findByRiskLinesId(Long id);
}
