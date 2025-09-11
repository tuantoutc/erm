package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.RiskAttributeLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskAttributeLineRepository extends JpaRepository<RiskAttributeLineEntity, Long> {

    List<RiskAttributeLineEntity> findByRiskId(Long id);
}
