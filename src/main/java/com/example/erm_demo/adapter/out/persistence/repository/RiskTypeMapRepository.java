package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.RiskTypeMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskTypeMapRepository extends JpaRepository<RiskTypeMapEntity,Long> {

    List<RiskTypeMapEntity> findByRiskTypeId(Long id);
    void deleteByRiskTypeId(Long id);
}

